package com.demo

import akka.actor.ActorSystem
import akka.testkit.{EventFilter, ImplicitSender, TestKit}
import com.demo.actors.statistics.Statistics
import com.demo.messages.Messages.{FailureMessage, RdsDecodeFailure}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class StatisticsSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("RGT-system"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "A Statistics" must {
    "log the failure message" in {
      val statistics = system.actorOf(Statistics.props())

      EventFilter.info(pattern = "Failure: Parse error", occurrences = 1) intercept {
        statistics ! FailureMessage("Parse error", RdsDecodeFailure, Option.empty)
      }
    }
  }

}
