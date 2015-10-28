package com.demo.actors.processor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.demo.domain.cision.Outlet
import com.demo.messages.Messages.{RabbitMessage, RdsReadyOutlet}
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._

import scala.util.{Failure, Success, Try}

class OutletProcessor(mapperSupervisor: ActorRef) extends Actor with ActorLogging {

  implicit val formats = DefaultFormats

  override def receive: Receive = {
    case RabbitMessage(metadata, body) =>
      val parsed = parse(new String(body, "UTF-8"))
      Try(parsed.extract[Outlet]) match {
        case Success(outlet) =>
          log.info("Get outlet: {}", outlet)
          mapperSupervisor ! RdsReadyOutlet(metadata, outlet)
        case Failure(ex) =>
          log.info("Error: {}", ex.getCause)
      }
  }
}

object OutletProcessor {
  val name = "outletProcessor"

  def props(mapperSupervisor: ActorRef) = Props(new OutletProcessor(mapperSupervisor))
}
