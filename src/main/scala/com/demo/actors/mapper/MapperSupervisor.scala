package com.demo.actors.mapper

import akka.actor.{Actor, ActorLogging, Props}
import com.demo.actors.sender.MojoApiSenderSupervisor
import com.demo.messages.Messages.RdsReadyContact

class MapperSupervisor extends Actor with ActorLogging {

  val senderSupervisor = context.actorOf(MojoApiSenderSupervisor.props(), MojoApiSenderSupervisor.name)
  val mapper = context.actorOf(Mapper.props(senderSupervisor), Mapper.name)

  override def receive: Receive = {
    case contact: RdsReadyContact =>
      mapper forward contact
  }
}

object MapperSupervisor {
  val name = "mapperSupervisor"

  def props() = Props(new MapperSupervisor)
}

