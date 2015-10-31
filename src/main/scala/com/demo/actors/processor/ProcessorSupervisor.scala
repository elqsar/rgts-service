package com.demo.actors.processor

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import com.demo.actors.mapper.MapperSupervisor
import com.demo.messages.Messages.{RabbitMessageOutlet, RabbitMessageContact, RabbitMessage}

import scala.concurrent.duration._

class ProcessorSupervisor extends Actor with ActorLogging {
  val mapperSupervisor = context.actorOf(MapperSupervisor.props(), MapperSupervisor.name)
  val processor = context.actorOf(Processor.props(mapperSupervisor), Processor.name)
  val decoder = context.actorOf(Decoder.props(processor), Decoder.name)

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 10, withinTimeRange = 60.seconds) {
    case _: Exception => Restart
  }

  override def receive: Receive = {
    case message: RabbitMessage =>
      decoder forward message
  }
}

object ProcessorSupervisor {
  val name = "processorSupervisor"

  def props() = Props(new ProcessorSupervisor)
}
