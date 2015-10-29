package com.demo.actors.sender

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.RoundRobinPool
import com.demo.messages.Messages.PostContactRequest

class MojoApiSenderSupervisor extends Actor with ActorLogging {

  val mojoSender = context.actorOf(RoundRobinPool(4).props(MojoApiSender.props()).withDispatcher("api"), MojoApiSender.name)

  override def receive: Receive = {
    case message: PostContactRequest =>
      mojoSender forward message
  }
}

object MojoApiSenderSupervisor {
  val name = "mojoApiSenderSupervisor"

  def props() = Props(new MojoApiSenderSupervisor)
}