package com.demo.actors.processor

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import com.demo.actors.mapper.MapperSupervisor
import com.demo.messages.Messages.RabbitMessage

import scala.concurrent.duration._

class ProcessorSupervisor extends Actor with ActorLogging {
  val mapperSupervisor = context.actorOf(MapperSupervisor.props(), "mapperSupervisor")
  val processor = context.actorOf(Processor.props(mapperSupervisor), "processor")

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 10, withinTimeRange = 60.seconds) {
    case _: Exception => Restart
  }

  override def receive: Receive = {
    case message: RabbitMessage =>
      processor forward message
  }
}

object ProcessorSupervisor {
  def props() = Props(new ProcessorSupervisor)
}
