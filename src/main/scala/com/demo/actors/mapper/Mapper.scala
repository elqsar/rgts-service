package com.demo.actors.mapper

import akka.actor.{Actor, ActorLogging, Props}
import com.demo.domain.MojoContactMapper
import com.demo.messages.Messages.RdsReadyContact

class Mapper extends Actor with ActorLogging {

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info("Starting new mappper")
  }

  override def receive: Receive = {
    case RdsReadyContact(contact) =>
      val mojoContact = MojoContactMapper(contact).map()
      log.info("Received new contact: {}", mojoContact)
  }
}

object Mapper {
  def props() = Props(new Mapper)
}
