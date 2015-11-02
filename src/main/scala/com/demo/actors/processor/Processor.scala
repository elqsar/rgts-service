package com.demo.actors.processor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.demo.actors.routes.ActorRoutes
import com.demo.domain.cision.{Contact, Outlet}
import com.demo.messages.Messages._
import org.json4s._

class Processor(mapperSupervisor: ActorRef) extends Actor with ActorLogging {
  implicit val formats = DefaultFormats

  val statistics = context.actorSelection(ActorRoutes.statisticsSupervisor)

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    statistics ! FailureMessage(reason.getMessage, RdsMappingFailure, message)
    super.preRestart(reason, message)
  }

  override def receive: Receive = {
    case ProcessContact(metadata, content) =>
      val contact = content.extract[Contact]
      mapperSupervisor ! RdsReadyContact(metadata, contact)

    case ProcessOutlet(metadata, content) =>
      val outlet = content.extract[Outlet]
      mapperSupervisor ! RdsReadyOutlet(metadata, outlet)
  }
}

object Processor {
  val name = "contactProcessor"

  def props(mapperSupervisor: ActorRef) = Props(new Processor(mapperSupervisor))
}

