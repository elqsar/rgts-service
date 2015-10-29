package com.demo.actors.requester

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern._
import com.demo.actors.breaker.EndpointGuard
import com.demo.actors.requester.Requester.SuccessContact
import com.demo.actors.sender.exceptions.MojoApiException
import com.demo.messages.Messages.{GetContactRequest, GetOutletRequest, RabbitMetadata}
import com.demo.webclient.WebClient

import scala.concurrent.Future

class Requester extends Actor with ActorLogging with EndpointGuard {

  val breaker = createCircuitBreaker(context.system.scheduler)

  implicit val exec = context.dispatcher

  override def receive: Receive = {
    case GetOutletRequest(id, metadata) =>
      breaker.withCircuitBreaker(getOutlet(createUrl("outlets", id), metadata)).pipeTo(sender())

    case GetContactRequest(id, metadata) =>
      breaker.withCircuitBreaker(getContact(createUrl("journalists", id), metadata)).pipeTo(sender())
  }

  def getOutlet(url: String, metadata: RabbitMetadata): Future[SuccessContact] = {
    val future = WebClient.get(url)

    future.map(response => SuccessContact(response.getStatusCode, response.getResponseBodyAsBytes)) recover {
      case t: Throwable => throw MojoApiException(t.getMessage)
    }
  }

  def getContact(url: String, metadata: RabbitMetadata): Future[SuccessContact] = {
    val future = WebClient.get(url)

    future.map(response => SuccessContact(response.getStatusCode, response.getResponseBodyAsBytes)) recover {
      case t: Throwable => throw MojoApiException(t.getMessage)
    }
  }

  def createUrl(url: String, id: Long): String = s"$url/$id"

  override def notifyCircuitBreakerOpen(): Unit = {

  }

  override def notifyCircuitBreakerClose(): Unit = {

  }

  override def notifyCircuitBreakerHalfOpen(): Unit = {

  }
}

object Requester {
  val name = "requester"

  case class SuccessContact(statusCode: Int, body: Array[Byte])

  def props() = Props(new Requester)
}
