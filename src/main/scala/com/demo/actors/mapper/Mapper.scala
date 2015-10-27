package com.demo.actors.mapper

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.demo.domain.MojoContactMapper
import com.demo.messages.Messages.{PostContactRequest, RdsReadyContact}

class Mapper(mojoApiSenderSupervisor: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case RdsReadyContact(metadata, contact) =>
      val mojoContact = MojoContactMapper(contact).map()
      log.info("Received new contact: {}", mojoContact)
      mojoApiSenderSupervisor ! PostContactRequest(metadata, mojoContact)
  }
}

object Mapper {
  def props(mojoApiSenderSupervisor: ActorRef) = Props(new Mapper(mojoApiSenderSupervisor))
}
