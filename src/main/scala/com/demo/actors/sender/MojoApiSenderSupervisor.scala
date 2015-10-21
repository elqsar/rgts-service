package com.demo.actors.sender

import akka.actor.{Actor, ActorLogging, Props}
import com.demo.messages.Messages.PostRequest

class MojoApiSenderSupervisor extends Actor with ActorLogging {
  
  val mojoSender = context.actorOf(MojoApiSender.props())
  
  override def receive: Receive = {
    case message: PostRequest =>
      mojoSender forward message
  }
}

object MojoApiSenderSupervisor {
  def props() = Props(new MojoApiSenderSupervisor)
}