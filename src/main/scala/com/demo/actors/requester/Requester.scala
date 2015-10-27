package com.demo.actors.requester

import akka.actor.{Actor, ActorLogging}

class Requester extends Actor with ActorLogging {
  override def receive: Receive = {
    case message =>
  }
}
