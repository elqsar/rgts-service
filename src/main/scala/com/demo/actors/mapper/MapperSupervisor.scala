package com.demo.actors.mapper

import akka.actor.{Actor, ActorLogging, Props}
import com.demo.messages.Messages.RdsReadyContact

class MapperSupervisor extends Actor with ActorLogging {

  val mapper = context.actorOf(Mapper.props(), "mapper")

  override def receive: Receive = {
    case contact: RdsReadyContact =>
      mapper forward contact
  }
}

object MapperSupervisor {
  def props() = Props(new MapperSupervisor)
}

