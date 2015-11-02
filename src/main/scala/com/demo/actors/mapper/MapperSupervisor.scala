package com.demo.actors.mapper

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import com.demo.actors.endpoint.{EndpointSupervisor, EndpointSupervisor$}
import com.demo.messages.Messages.RdsReadyContact
import scala.concurrent.duration._

class MapperSupervisor extends Actor with ActorLogging {
  val senderSupervisor = context.actorOf(EndpointSupervisor.props(), EndpointSupervisor.name)
  val mapper = context.actorOf(Mapper.props(senderSupervisor), Mapper.name)

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 5, withinTimeRange = 60.seconds) {
    case _: Exception => Restart
  }

  override def receive: Receive = {
    case contact: RdsReadyContact =>
      mapper forward contact
  }
}

object MapperSupervisor {
  val name = "mapperSupervisor"

  def props() = Props(new MapperSupervisor)
}

