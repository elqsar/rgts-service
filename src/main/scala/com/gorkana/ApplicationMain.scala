package com.gorkana

import akka.actor.ActorSystem
import com.gorkana.actors.ReceptionistSupervisor
import kamon.Kamon

object ApplicationMain extends App {
  Kamon.start()

  val system = ActorSystem("RGDS-system")

  val receptionistSupervisor = system.actorOf(ReceptionistSupervisor.props(), "supervisor")
}