package com.gorkana.actors

import akka.actor.{Props, Actor, ActorLogging}
import akka.routing.RoundRobinPool
import com.gorkana.messages.Messages._

class ProcessorSupervisor extends Actor with ActorLogging {
  val processor = context.actorOf(RoundRobinPool(4).props(Processor.props()), "processor")

  override def receive: Receive = {
    case message: RabbitMessage =>
      processor forward message
  }
}

object ProcessorSupervisor {
  def props() = Props(new ProcessorSupervisor)
}
