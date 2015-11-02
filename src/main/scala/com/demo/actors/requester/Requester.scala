package com.demo.actors.requester

import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import akka.pattern._
import com.demo.actors.breaker.EndpointGuard
import com.demo.actors.requester.Requester.{SuccessOutlet, SuccessContact}
import com.demo.actors.endpoint.exceptions.MojoApiException
import com.demo.messages.Messages.{DecodeMojoContact, GetContactRequest, GetOutletRequest, RabbitMetadata}
import com.demo.webclient.WebClient

import scala.concurrent.Future

class Requester(mojoDecoder: ActorRef) extends Actor with ActorLogging with EndpointGuard {
  implicit val exec = context.dispatcher

  val breaker = createCircuitBreaker(context.system.scheduler)
  val outletsUrl = "outlets"
  val contactUrl = "journalists"

  override def receive: Receive = {
    case GetOutletRequest(id, metadata) =>
      breaker.withCircuitBreaker(getOutlet(createUrl(outletsUrl, id), metadata)).pipeTo(self)

    case GetContactRequest(id, metadata) =>
      breaker.withCircuitBreaker(getContact(createUrl(contactUrl, id), metadata)).pipeTo(self)

    case SuccessContact(metadata, contact) =>

  }

  def getOutlet(url: String, metadata: RabbitMetadata): Future[SuccessOutlet] = {
    val future = WebClient.get(url)

    future.map(response => SuccessOutlet(metadata, response.getResponseBodyAsBytes)) recover {
      case t: Throwable => throw MojoApiException(t.getMessage)
    }
  }

  def getContact(url: String, metadata: RabbitMetadata): Future[SuccessContact] = {
    val future = WebClient.get(url)

    future.map(response => SuccessContact(metadata, response.getResponseBodyAsBytes)) recover {
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

  case class SuccessContact(metadata: RabbitMetadata, body: Array[Byte])
  case class SuccessOutlet(metadata: RabbitMetadata, body: Array[Byte])

  def props(mojoDecoder: ActorRef) = Props(new Requester(mojoDecoder))
}
