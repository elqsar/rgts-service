package com.demo

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.demo.actors.requester.Requester
import com.demo.actors.requester.Requester.{SuccessContact, SuccessOutlet}
import com.demo.messages.Messages.{BatchQueue, GetContactRequest, GetOutletRequest, RabbitMetadata}
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
      val requester = system.actorOf(Props(new Requester {
        override def getContact(url: String, metadata: RabbitMetadata): Future[SuccessContact] = {
          Future.successful(SuccessContact(metadata, "Success".getBytes))
        }
      }))
      val metadata = RabbitMetadata(BatchQueue, "Contact", 1L)

      requester ! GetContactRequest(1L, metadata)

      val response = expectMsgType[SuccessContact]

      response.metadata should be(metadata)
      response.body should be("Success".getBytes)
    }
  }

  "A requester" must {
    "return Outlet to sender" in {
      val requester = system.actorOf(Props(new Requester {
        override def getOutlet(url: String, metadata: RabbitMetadata): Future[SuccessOutlet] = {
          Future.successful(SuccessOutlet(metadata, "Success".getBytes))
        }
      }))
      val metadata = RabbitMetadata(BatchQueue, "Outlet", 1L)

      requester ! GetOutletRequest(1L, metadata)

      val response = expectMsgType[SuccessOutlet]

      response.metadata should be(metadata)
      response.body should be("Success".getBytes)
    }
  }
}
