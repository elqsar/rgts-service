package com.demo.actors.endpoint

import akka.actor.SupervisorStrategy.Restart
import akka.actor._
import akka.routing.RoundRobinPool
import com.demo.messages.Messages.MojoRequest

import scala.concurrent.duration._

class EndpointSupervisor extends Actor with ActorLogging {
  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 10, withinTimeRange = 60.seconds) {
    case _: Exception => Restart
  }

  val endpoint = context.actorOf(RoundRobinPool(4, supervisorStrategy = supervisorStrategy).props(Endpoint.props()).withDispatcher("api"), Endpoint.name)

  override def receive: Receive = {
    case message: MojoRequest =>
      endpoint forward message
  }
}

object EndpointSupervisor {
  val name = "endpointSupervisor"

  def props() = Props(new EndpointSupervisor)
}