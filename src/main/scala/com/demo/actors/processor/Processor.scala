package com.demo.actors.processor

import akka.actor.{ActorRef, Actor, ActorLogging, Props}
import com.demo.domain._
import com.demo.messages.Messages.{RdsReadyContact, RabbitMessage}
import org.json4s._
import org.json4s.jackson.JsonMethods._

import scala.util.{Failure, Success, Try}

class Processor(mapperSupervisor: ActorRef) extends Actor with ActorLogging {

  implicit val formats = DefaultFormats

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info("Starting new processor")
  }

  override def receive: Receive = {
    case message@RabbitMessage(mediaType, body) =>
      val parsed = parse(new String(body, "UTF-8"))
      Try(parsed.extract[Contact]) match {
        case Success(contact) =>
          log.info("Get contact: {}", contact)
          mapperSupervisor ! RdsReadyContact(contact)
        case Failure(ex) =>
          log.info("Error: {}", ex.getCause)
      }
  }
}

object Processor {
  def props(mapperSupervisor: ActorRef) = Props(new Processor(mapperSupervisor))
}

