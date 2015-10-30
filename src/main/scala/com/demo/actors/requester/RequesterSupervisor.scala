package com.demo.actors.requester

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import com.demo.messages.Messages.{GetOutletRequest, GetContactRequest}
import scala.concurrent.duration._

class RequesterSupervisor extends Actor with ActorLogging {
  val requester = context.actorOf(Requester.props().withDispatcher("api"), Requester.name)

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 5, withinTimeRange = 60.seconds) {
    case _: Exception => Restart
  }

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
