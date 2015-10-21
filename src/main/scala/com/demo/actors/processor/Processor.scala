package com.demo.actors.processor

import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import com.demo.domain._
import com.demo.messages.Messages.{RdsReadyContact, RabbitMessage}
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.{Failure, Success, Try}

class Processor(mapperSupervisor: ActorRef) extends Actor with ActorLogging {

  implicit val formats = DefaultFormats

  override def receive: Receive = {
    case message@RabbitMessage(metadata, body) =>
      val parsed = parse(new String(body, "UTF-8"))
      Try(parsed.extract[Contact]) match {
        case Success(contact) =>
          log.info("Get contact: {}", contact)
          mapperSupervisor ! RdsReadyContact(metadata, contact)
        case Failure(ex) =>
          log.info("Error: {}", ex.getCause)
      }
  }
}

object Processor {
  def props(mapperSupervisor: ActorRef) = Props(new Processor(mapperSupervisor))
}

