package com.demo.actors.requester

import akka.actor.{Props, Actor, ActorLogging}
import akka.pattern._
import com.demo.actors.breaker.EndpointGuard
import com.demo.messages.Messages.{GetContactRequest, GetOutletRequest}
import com.demo.webclient.WebClient
import com.ning.http.client.Response

import scala.concurrent.Future

class Requester extends Actor with ActorLogging with EndpointGuard {

  val breaker = createBreaker(context.system.scheduler)

  implicit val exec = context.dispatcher

  override def receive: Receive = {
    case GetOutletRequest(url, id) =>
      breaker.withCircuitBreaker(getOutlet(createUrl(url, id))).pipeTo(self)

    case GetContactRequest(url, id) =>
      breaker.withCircuitBreaker(getContact(createUrl(url, id))).pipeTo(self)
  }

  def getOutlet(url: String): Future[Response] = {
    WebClient.get(url) recover {
      case t: Throwable => throw new Exception
    }
  }

  def getContact(url: String): Future[Response] = {
    WebClient.get(url) recover {
      case t: Throwable => throw new Exception
    }
  }

  def createUrl(url: String, id: String): String = url + "/" + id

  override def notifyCircuitBreakerOpen(): Unit = {

  }

  override def notifyCircuitBreakerClose(): Unit = {

  }

  override def notifyCircuitBreakerHalfOpen(): Unit = {

  }
}

object Requester {
  case class SuccessContact(response: Response)
  def props() = Props(new Requester)
}
