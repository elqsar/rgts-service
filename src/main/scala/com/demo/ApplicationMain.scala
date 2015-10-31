package com.demo

import akka.actor.{ActorSystem, DeadLetter}
import com.demo.actors.consumer.ConsumerSupervisor
import com.demo.actors.statistics.StatisticsSupervisor
import com.demo.actors.system.DeadLettersChecker
import com.demo.messages.Messages.StartConsume

object ApplicationMain extends App {
  //Kamon.start()

  val system = ActorSystem("RGT-system")

  val consumerSupervisor = system.actorOf(ConsumerSupervisor.props(), ConsumerSupervisor.name)
  val statisticsSupervisor = system.actorOf(StatisticsSupervisor.props(), StatisticsSupervisor.name)

  // dead letter checker just for test now
  val deadLetterChecker = system.actorOf(DeadLettersChecker.props())
  system.eventStream.subscribe(deadLetterChecker, classOf[DeadLetter])

  consumerSupervisor ! StartConsume
}