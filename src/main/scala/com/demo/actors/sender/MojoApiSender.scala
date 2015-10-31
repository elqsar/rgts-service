package com.demo.actors.sender

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern._
import com.demo.actors.breaker.EndpointGuard
import com.demo.actors.routes.ActorRoutes
import com.demo.actors.sender.exceptions.MojoApiException
import com.demo.messages.Messages._
import com.demo.webclient.WebClient

import scala.concurrent.Future

class MojoApiSender extends Actor with ActorLogging with EndpointGuard {
  implicit val exec = context.dispatcher

  val breaker = createCircuitBreaker(context.system.scheduler)
  val consumer = context.actorSelection(ActorRoutes.consumerSupervisor)

  override def receive: Receive = {
    case PostContactRequest(id, metadata, mojoContact) =>
      breaker.withCircuitBreaker(postContact(id, metadata, mojoContact)).pipeTo(self)
    case PostOutletRequest(id, metadata, mojoOutlet) =>

    case PutContactRequest(id, metadata, mojoContact) =>
      breaker.withCircuitBreaker(putContact(id, metadata, mojoContact)).pipeTo(self)
    case PutOutletRequest(id, metadata, mojoOutlet) =>

    case SuccessResponse(metadata, response) =>
      log.info("Success response: {} with tag: {}", response.getStatusCode, metadata.deliveryTag)
      consumer ! ProcessAck(metadata.queueType, metadata.deliveryTag)
    case FailedResponse(metadata, response) =>
      log.info("Failed response: {} with tag: {}", response.getStatusCode, metadata.deliveryTag)
    case CheckHealth =>
      checkHealth()
  }

  def postContact(id: Long, metadata: RabbitMetadata, content: String): Future[SuccessResponse] = {
    log.info("POST MOJO contact: {}", content)
    val future = WebClient.post(s"journalists/$id", content)
    future.map { response =>
      SuccessResponse(metadata, response)
    }.recover {
      case e: Throwable =>
        log.info("API Error: {}", e.getMessage)
        throw MojoApiException(e.getMessage)
    }
  }

  def putContact(id: Long, metadata: RabbitMetadata, content: String): Future[SuccessResponse] = {
    log.info("PUT MOJO contact: {}", content)
    val future = WebClient.put(s"journalists/$id", content)
    future.map { response =>
      SuccessResponse(metadata, response)
    }.recover {
      case e: Throwable =>
        log.info("API Error: {}", e.getMessage)
        throw MojoApiException(e.getMessage)
    }
  }

  def checkHealth(): Unit = {
    breaker.withCircuitBreaker(WebClient.get("memo-types").recover {
      case e: Throwable =>
        throw new Exception()
    })
  }

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

object MojoApiSender {
  val name = "mojoApiSender"

  def props() = Props(new MojoApiSender)
}
