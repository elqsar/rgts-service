package com.demo.actors.endpoint

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern._
import com.demo.actors.breaker.EndpointGuard
import com.demo.actors.routes.ActorRoutes
import com.demo.actors.endpoint.exceptions.MojoApiException
import com.demo.domain.mojo.{MojoOutlet, MojoContact}
import com.demo.messages.Messages._
import com.demo.webclient.WebClient
import org.json4s.NoTypeHints
import org.json4s.native.Serialization

import scala.concurrent.Future

class Endpoint extends Actor with ActorLogging with EndpointGuard {
  implicit val exec = context.dispatcher
  implicit val formats = Serialization.formats(NoTypeHints)

  val breaker = createCircuitBreaker(context.system.scheduler)
  val consumer = context.actorSelection(ActorRoutes.consumerSupervisor)

  override def receive: Receive = {
    case PostContactRequest(metadata, mojoContact) =>
      breaker.withCircuitBreaker(postContact(metadata, mojoContact)).pipeTo(self)

    case PostOutletRequest(metadata, mojoOutlet) =>

    case PutContactRequest(metadata, mojoContact) =>
      breaker.withCircuitBreaker(putContact(metadata, mojoContact)).pipeTo(self)

    case PutOutletRequest(metadata, mojoOutlet) =>

    case SuccessResponse(metadata, response) =>
      log.info("Success response: {} with tag: {}", response.getStatusCode, metadata.deliveryTag)
      consumer ! ProcessAck(metadata.queueType, metadata.deliveryTag)

    case FailedResponse(metadata, response) =>
      log.info("Failed response: {} with tag: {}", response.getStatusCode, metadata.deliveryTag)

    case CheckHealth =>
      checkHealth()
  }

  def postContact(metadata: RabbitMetadata, contact: MojoContact): Future[SuccessResponse] = {
    val content = Serialization.write(contact)
    val future = WebClient.post(s"journalists/${contact.id}", content)
    future.map { response =>
      SuccessResponse(metadata, response)
    }.recover {
      case e: Throwable =>
        log.info("API Error: {}", e.getMessage)
        throw MojoApiException(e.getMessage)
    }
  }

  def putContact(metadata: RabbitMetadata, contact: MojoContact): Future[SuccessResponse] = {
    val content = Serialization.write(contact)
    val future = WebClient.put(s"journalists/${contact.id}", content)
    future.map { response =>
      SuccessResponse(metadata, response)
    }.recover {
      case e: Throwable =>
        log.info("API Error: {}", e.getMessage)
        throw MojoApiException(e.getMessage)
    }
  }

  def postOutlet(metadata: RabbitMetadata, outlet: MojoOutlet): Future[SuccessResponse] = {
    val content = Serialization.write(outlet)
    val future = WebClient.post(s"outlets/${outlet.id}", content)
    future.map { response =>
      SuccessResponse(metadata, response)
    }.recover {
      case e: Throwable =>
        log.info("API Error: {}", e.getMessage)
        throw MojoApiException(e.getMessage)
    }
  }

  def putOutlet(metadata: RabbitMetadata, outlet: MojoOutlet): Future[SuccessResponse] = {
    val content = Serialization.write(outlet)
    val future = WebClient.put(s"outlets/${outlet.id}", content)
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

object Endpoint {
  val name = "endpoint"

  def props() = Props(new Endpoint)
}
