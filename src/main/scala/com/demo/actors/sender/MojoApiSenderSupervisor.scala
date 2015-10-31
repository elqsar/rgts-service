package com.demo.actors.sender

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.RoundRobinPool
import com.demo.messages.Messages.{EncodeContact, PostContactRequest}

class MojoApiSenderSupervisor extends Actor with ActorLogging {
  val mojoSender = context.actorOf(RoundRobinPool(4).props(MojoApiSender.props()).withDispatcher("api"), MojoApiSender.name)
  val encoder = context.actorOf(Encoder.props(mojoSender), Encoder.name)

  override def receive: Receive = {
    case message: EncodeContact =>
      encoder forward message
  }
}

object MojoApiSenderSupervisor {
  val name = "mojoApiSenderSupervisor"

  def props() = Props(new MojoApiSenderSupervisor)
}