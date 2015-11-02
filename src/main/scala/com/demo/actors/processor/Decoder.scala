package com.demo.actors.processor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.demo.actors.routes.ActorRoutes
import com.demo.messages.Messages._
import org.json4s._
import org.json4s.jackson.JsonMethods._

class Decoder(processor: ActorRef) extends Actor with ActorLogging {
  implicit val formats = DefaultFormats

  val statistics = context.actorSelection(ActorRoutes.statisticsSupervisor)

  @throws[Exception](classOf[Exception])
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    statistics ! FailureMessage(reason.getMessage, RdsDecodeFailure, message)
    super.preRestart(reason, message)
  }

  override def receive: Receive = {
    case RabbitMessageOutlet(metadata, rawOutlet) =>
      val json = toJson(rawOutlet)
      processor ! ProcessOutlet(metadata, json)

    case RabbitMessageContact(metadata, rawContact) =>
      val json = toJson(rawContact)
      processor ! ProcessContact(metadata, json)
  }

  private def toJson(body: Array[Byte]) = parse(new String(body, "UTF-8"))
}

object Decoder {
  val name = "decoder"

  def props(processor: ActorRef) = Props(new Decoder(processor))
}
