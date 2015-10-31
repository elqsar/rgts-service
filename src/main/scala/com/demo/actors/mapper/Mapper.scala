package com.demo.actors.mapper

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.demo.domain.mojo.MojoContactMapper
import com.demo.messages.Messages.{EncodeContact, PostContactRequest, RdsReadyContact}

class Mapper(mojoApiSenderSupervisor: ActorRef) extends Actor with ActorLogging {

  override def receive: Receive = {
    case RdsReadyContact(metadata, contact) =>
      val mojoContact = MojoContactMapper(contact)
      log.info("Received new contact: {}", mojoContact)
      mojoApiSenderSupervisor ! EncodeContact(metadata, mojoContact)
  }
}

object Mapper {
  val name = "mapper"

  def props(mojoApiSenderSupervisor: ActorRef) = Props(new Mapper(mojoApiSenderSupervisor))
}
