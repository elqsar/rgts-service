package com.demo.actors.requester

import akka.actor.{Props, Actor, ActorLogging}

class RequesterSupervisor extends Actor with ActorLogging {
  override def receive: Receive = {
    case message =>
  }
}

object RequesterSupervisor {
  def props() = Props(new RequesterSupervisor)
}
