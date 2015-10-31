package com.demo.actors.sender

import akka.actor.{Props, ActorRef, Actor, ActorLogging}
import com.demo.actors.routes.ActorRoutes
import com.demo.domain.mojo.{MojoOutlet, MojoContact}
import com.demo.messages.Messages._
import org.json4s.NoTypeHints
import org.json4s.native.Serialization

import scala.util.{Failure, Success, Try}

class Encoder(endpoint: ActorRef) extends Actor with ActorLogging {
  implicit val formats = Serialization.formats(NoTypeHints)

  val statistics = context.actorSelection(ActorRoutes.statisticsSupervisor)

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    statistics ! FailureMessage(reason.getMessage, MojoEncodeFailure, message)
    super.preRestart(reason, message)
  }

  override def receive: Receive = {
    case EncodeContact(metadata: RabbitMetadata, contact: MojoContact) =>
      val content = Serialization.write(contact)
      endpoint ! PostContactRequest(contact.id, metadata, content)

    case EncodeOutlet(metadata: RabbitMetadata, outlet: MojoOutlet) =>
      val content = Serialization.write(outlet)
      endpoint ! PostOutletRequest(outlet.id, metadata, content)
  }
}

object Encoder {
  val name = "encoder"

  def props(endpoint: ActorRef) = Props(new Encoder(endpoint))
}
