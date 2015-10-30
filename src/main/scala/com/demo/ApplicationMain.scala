package com.demo

import akka.actor.{DeadLetter, ActorSystem}
import com.demo.actors.consumer.ConsumerSupervisor
import com.demo.actors.consumer.ConsumerSupervisor.{StopConsume, StartConsume}
import com.demo.actors.system.DeadLettersChecker

object ApplicationMain extends App {
  //Kamon.start()

  val system = ActorSystem("RGT-system")

  val consumerSupervisor = system.actorOf(ConsumerSupervisor.props(), ConsumerSupervisor.name)

  // dead letter checker just for test now
  val deadLetterChecker = system.actorOf(DeadLettersChecker.props())
  system.eventStream.subscribe(deadLetterChecker, classOf[DeadLetter])

  consumerSupervisor ! StartConsume
}