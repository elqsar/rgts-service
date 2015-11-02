package com.demo.actors.requester

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern._
import com.demo.actors.breaker.EndpointGuard
import com.demo.actors.endpoint.exceptions.MojoApiException
import com.demo.actors.routes.ActorRoutes
import com.demo.messages.Messages._
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

    case SuccessGetContact(metadata, contact) =>

    case SuccessGetOutlet(metadata, outlet) =>

    case CheckHealth =>
      checkHealth()

  }

  def getOutlet(url: String, metadata: RabbitMetadata): Future[SuccessGetOutlet] = {
    val future = WebClient.get(url)

    future.map(response => SuccessGetOutlet(metadata, response.getResponseBodyAsBytes)) recover {
      case t: Throwable => throw MojoApiException(t.getMessage)
    }
  }

  def getContact(url: String, metadata: RabbitMetadata): Future[SuccessGetContact] = {
    val future = WebClient.get(url)

    future.map(response => SuccessGetContact(metadata, response.getResponseBodyAsBytes)) recover {
      case t: Throwable => throw MojoApiException(t.getMessage)
    }
  }

  def checkHealth(): Unit = {
    breaker.withCircuitBreaker(WebClient.get("memo-types").recover {
      case e: Throwable =>
        throw new Exception()
    })
  }

  def createUrl(url: String, id: Long): String = s"$url/$id"

  override def notifyCircuitBreakerOpen(): Unit = {
    log.info("Circuit breaker is opened")
    context.actorSelection(ActorRoutes.consumerSupervisor) ! StopConsume
  }

  override def notifyCircuitBreakerClose(): Unit = {
    log.info("Circuit breaker is closed")
    context.actorSelection(ActorRoutes.consumerSupervisor) ! StartConsume
  }

  override def notifyCircuitBreakerHalfOpen(): Unit = {
    log.info("Checking endpoint health")
    self ! CheckHealth
  }
}

object Requester {
  val name = "requester"

  def props(mojoDecoder: ActorRef) = Props(new Requester(mojoDecoder))
}
