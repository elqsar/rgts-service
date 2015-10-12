package com.demo.actors

import akka.actor.{Props, Actor, ActorLogging}
import com.demo.domain.Contact
import com.demo.messages.Messages
import Contact
import Messages.RabbitMessage
import org.json4s.jackson.JsonMethods._
import org.json4s._

import scala.util.{Failure, Success, Try}

class Processor extends Actor with ActorLogging {

  implicit val formats = DefaultFormats

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info("Starting new processor")
  }

  override def receive: Receive = {
    case message @ RabbitMessage(body) =>
      val parsed = parse(new String(body, "UTF-8"))
      Try(parsed.extract[Contact]) match {
        case Success(contact) =>
          log.info("Recieved new contact: {}", contact)
        case Failure(ex) =>
          log.info("Error: {}", ex.getCause)
      }
  }
}

object Processor {
  def props() = Props(new Processor)
}

