package com.demo.actors.statistics

import akka.actor.{Props, Actor, ActorLogging}
import com.demo.messages.Messages.FailureMessage

class Statistics extends Actor with ActorLogging {
  override def receive: Receive = {
    case FailureMessage(errorMessage, failure, message) =>
      // here we need to collect this info to some kind of log or journal to analyze later
      log.info("Failure: {} {}", errorMessage, failure)
  }
}

object Statistics {
  val name = "statistics"

  def props() = Props(new Statistics)
}
