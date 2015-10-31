package com.demo.actors.statistics

import akka.actor.{Props, Actor, ActorLogging}
import com.demo.messages.Messages.FailureMessage

class StatisticsSupervisor extends Actor with ActorLogging {
  val statistics = context.actorOf(Statistics.props(), Statistics.name)

  override def receive: Receive = {
    case message: FailureMessage =>
      statistics forward message
  }
}

object StatisticsSupervisor {
  val name = "statisticsSupervisor"

  def props() = Props(new StatisticsSupervisor)
}
