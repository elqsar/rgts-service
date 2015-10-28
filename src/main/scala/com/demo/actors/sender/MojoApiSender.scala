package com.demo.actors.sender

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern._
import com.demo.actors.breaker.EndpointGuard
import com.demo.actors.consumer.ConsumerSupervisor.{StartConsume, StopConsume}
import com.demo.actors.routes.ActorRoutes
import com.demo.actors.sender.MojoApiSender.CheckHealth
import com.demo.domain.mojo.MojoContact
import com.demo.messages.Messages.{PostContactRequest, ProcessAck, RabbitMetadata, SuccessResponse}
import com.demo.webclient.WebClient

import scala.concurrent.Future

class MojoApiSender extends Actor with ActorLogging with EndpointGuard {

  implicit val exec = context.dispatcher

  val breaker = createBreaker(context.system.scheduler)

  override def receive: Receive = {
    case PostContactRequest(metadata, mojoContact) =>
      breaker.withCircuitBreaker(sendData(metadata, mojoContact)).pipeTo(self)
    case SuccessResponse(metadata, response) =>
      log.info("Success response: " + response.getStatusCode)
      context.actorSelection(ActorRoutes.consumerSupervisor) ! ProcessAck(metadata.queueType, metadata.deliveryTag)
    case CheckHealth =>
      checkHealth()
  }

  def sendData(metadata: RabbitMetadata, contact: MojoContact): Future[SuccessResponse] = {
    val f = WebClient.get("memo-types")
    f.map { response =>
      SuccessResponse(metadata, response)
    }.recover {
      case e: Throwable =>
        throw new Exception()
    }
  }

  def checkHealth(): Unit = {
    breaker.withCircuitBreaker(WebClient.get("http://localhost:3001/memo-types").recover {
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
  case object CheckHealth
  val name = "mojoApiSender"

  def props() = Props(new MojoApiSender)
}
