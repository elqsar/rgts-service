package com.demo.actors.requester

import akka.actor.{Props, Actor, ActorLogging}
import com.demo.messages.Messages.{DecodeMojoOutlet, DecodeMojoContact}
import org.json4s.DefaultFormats

class MojoDecoder extends Actor with ActorLogging {
  implicit val formats = DefaultFormats

  override def receive: Receive = {
    case DecodeMojoContact(metadata, contact) =>

    case DecodeMojoOutlet(metadata, outlet) =>
  }
}

object MojoDecoder {
  val name = "mojoDecoder"

  def props() = Props(new MojoDecoder)
}
