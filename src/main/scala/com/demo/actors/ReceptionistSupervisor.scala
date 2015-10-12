package com.demo.actors

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import com.demo.messages.Messages
import com.demo.messages.Messages.BreakMe
import scala.concurrent.duration._

class ReceptionistSupervisor extends Actor with ActorLogging {
  val processorSupervisor = context.actorOf(ProcessorSupervisor.props(), "processorSupervisor")
  val receptionist = context.actorOf(Receptionist.props(processorSupervisor), "receptionist")

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 5, withinTimeRange = 60 seconds) {
    case _: Exception => Restart
  }

  override def receive: Receive = {
    case BreakMe =>
  }
}

object ReceptionistSupervisor {
  def props() = Props(new ReceptionistSupervisor)
}
