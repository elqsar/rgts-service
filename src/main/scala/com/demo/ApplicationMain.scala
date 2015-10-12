package com.demo

import akka.actor.ActorSystem
import com.demo.actors.ReceptionistSupervisor
import kamon.Kamon

object ApplicationMain extends App {
  //Kamon.start()

  val system = ActorSystem("RGDS-system")

  val receptionistSupervisor = system.actorOf(ReceptionistSupervisor.props(), "supervisor")
}