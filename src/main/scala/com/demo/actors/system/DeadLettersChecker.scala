package com.demo.actors.system

import akka.actor.{Props, DeadLetter, Actor, ActorLogging}

class DeadLettersChecker extends Actor with ActorLogging {
  override def receive: Receive = {
    case message: DeadLetter =>
      log.info("We have a dead letter here: {}", message)
  }
}

object DeadLettersChecker {
  def props() = Props(new DeadLettersChecker)
}
