package com.demo

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.demo.actors.consumer.ConsumerSupervisor.BatchQueue
import com.demo.actors.requester.Requester
import com.demo.actors.requester.Requester.SuccessContact
import com.demo.messages.Messages.{GetContactRequest, RabbitMetadata}
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
          Future.successful(SuccessContact(200, "Success".getBytes))
        }
      }))

      requester ! GetContactRequest(1L, RabbitMetadata(BatchQueue, "Contact", 1L))

      val response = expectMsgType[SuccessContact]

      response.statusCode should be(200)
      response.body should be("Success".getBytes)
    }
  }
}
