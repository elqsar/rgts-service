package com.demo.actors.requester

import akka.actor.{Props, Actor, ActorLogging}
import com.demo.messages.Messages.{GetOutletRequest, GetContactRequest}

class RequesterSupervisor extends Actor with ActorLogging {
  val requester = context.actorOf(Requester.props(), Requester.name)

  override def receive: Receive = {
    case message: GetContactRequest =>
      requester forward message

    case message: GetOutletRequest =>
      requester forward message
  }
}

object RequesterSupervisor {
  def props() = Props(new RequesterSupervisor)
}
