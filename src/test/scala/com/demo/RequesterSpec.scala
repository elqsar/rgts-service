package com.demo

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.demo.actors.requester.Requester
import com.demo.messages.Messages.{SuccessGetContact, SuccessGetOutlet, BatchQueue, RabbitMetadata}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Future

class RequesterSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("RGT-system"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "A requester" must {
    "return Contact to sender" in {
      val probe = TestProbe()
      val requester = system.actorOf(Props(new Requester(probe.ref) {
        override def getContact(url: String, metadata: RabbitMetadata): Future[SuccessGetContact] = {
          Future.successful(SuccessGetContact(metadata, "Success".getBytes))
        }
      }))
      val metadata = RabbitMetadata(BatchQueue, "Contact", 1L)
    }
  }

  "A requester" must {
    "return Outlet to sender" in {
      val probe = TestProbe()
      val requester = system.actorOf(Props(new Requester(probe.ref) {
        override def getOutlet(url: String, metadata: RabbitMetadata): Future[SuccessGetOutlet] = {
          Future.successful(SuccessGetOutlet(metadata, "Success".getBytes))
        }
      }))
      val metadata = RabbitMetadata(BatchQueue, "Outlet", 1L)
    }
  }
}
