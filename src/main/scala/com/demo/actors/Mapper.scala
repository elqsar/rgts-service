package com.demo.actors

import akka.actor.{Props, Actor, ActorLogging}

class Mapper extends Actor with ActorLogging {
  override def receive: Receive = {
    case message =>
  }
}

object Mapper {
  def props() = Props(new Mapper)
}
