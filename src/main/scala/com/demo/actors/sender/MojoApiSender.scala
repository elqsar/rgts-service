package com.demo.actors.sender

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern._
import com.demo.actors.breaker.EndpointGuard
import com.demo.actors.consumer.ConsumerSupervisor.{StartConsume, StopConsume}
import com.demo.actors.routes.ActorRoutes
import com.demo.domain.MojoContact
import com.demo.messages.Messages._
import com.demo.webclient.WebClient

import scala.concurrent.Future

class MojoApiSender extends Actor with ActorLogging with EndpointGuard {
  implicit val exec = context.dispatcher

  val breaker = createCircuitBreaker(context.system.scheduler)

  override def receive: Receive = {
    case PostContactRequest(metadata, mojoContact) =>
      breaker.withCircuitBreaker(sendData(metadata, mojoContact)).pipeTo(self)
    case SuccessResponse(metadata, response) =>
      log.info("Success response: {} with tag: {}", response.getStatusCode, metadata.deliveryTag )
      context.actorSelection(ActorRoutes.consumerSupervisor) ! ProcessAck(metadata.queueType, metadata.deliveryTag)
    case CheckHealth =>
      checkHealth()
  }

  def sendData(metadata: RabbitMetadata, contact: MojoContact): Future[SuccessResponse] = {
    log.info("Sending data to API: {}", metadata.deliveryTag)
    val f = WebClient.get("journalists/516758")
    f.map { response =>
      SuccessResponse(metadata, response)
    }.recover {
      case e: Throwable =>
        throw new Exception()
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
