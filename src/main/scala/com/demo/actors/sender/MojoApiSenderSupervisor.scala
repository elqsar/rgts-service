package com.demo.actors.sender

import akka.actor.{Actor, ActorLogging, Props}
import com.demo.messages.Messages.PostContactRequest

class MojoApiSenderSupervisor extends Actor with ActorLogging {

  val mojoSender = context.actorOf(MojoApiSender.props())

  override def receive: Receive = {
    case message: PostContactRequest =>
      mojoSender forward message
  }
}

object MojoApiSenderSupervisor {
  def props() = Props(new MojoApiSenderSupervisor)
}