package com.demo.actors.sender

import akka.actor.{Actor, ActorLogging, Props}

class MojoApiSenderSupervisor extends Actor with ActorLogging {
  
  val mojoSender = context.actorOf(MojoApiSender.props())
  
  override def receive: Receive = {
    case message =>
  }
}

object MojoApiSenderSupervisor {
  def props() = Props(new MojoApiSenderSupervisor)
}