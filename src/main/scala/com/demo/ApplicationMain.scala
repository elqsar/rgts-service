package com.demo

import akka.actor.ActorSystem
import com.demo.actors.consumer.ConsumerSupervisor

object ApplicationMain extends App {
  //Kamon.start()

  val system = ActorSystem("RGDS-system")

  val consumerSupervisor = system.actorOf(ConsumerSupervisor.props(), "consumerSupervisor")
}