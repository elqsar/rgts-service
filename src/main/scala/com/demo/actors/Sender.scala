package com.demo.actors

import akka.actor.{Props, Actor, ActorLogging}

class Sender extends Actor with ActorLogging {
  override def receive: Receive = {
    case message =>
  }
}

object Sender {
  def props() = Props(new Sender)
}
