package com.demo.actors.sender

import akka.actor.{Actor, ActorLogging, Props}
import com.demo.domain.MojoContact
import com.demo.messages.Messages.PostRequest

import scala.concurrent.Future
import akka.pattern._

class MojoApiSender extends Actor with ActorLogging {

  // it will need separate dispatcher later
  implicit val exec = context.dispatcher

  override def receive: Receive = {
    case PostRequest(mojoContact) =>
      sendData(mojoContact) pipeTo self
  }

  def sendData(contact: MojoContact): Future[String] = ???
}

object MojoApiSender {
  def props() = Props(new MojoApiSender)
}
