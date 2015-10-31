package com.demo

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import com.demo.actors.consumer.ConsumerSupervisor
import com.demo.actors.consumer.ConsumerSupervisor._
import com.demo.messages.Messages._
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Envelope
import com.thenewmotion.akka.rabbitmq.ChannelMessage
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.collection.JavaConversions.mapAsJavaMap

class ConsumerSpec(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
with WordSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("RGT-system"))

  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  "A consumer" must {
    "send a proper message to processor supervisor" in {
      val probe = TestProbe()
      val consumer = TestActorRef(Props(new ConsumerSupervisor {
        override val processorSupervisor: ActorRef = probe.ref
      }))
      val envelope = new Envelope(1L, false, "", "")
      val properties = new BasicProperties()
      properties.setHeaders(Map("x-cision-record-type" -> "Contact"))
      val body =
        """
          |{
          |  "Id": 2045883,
          |  "OutletId": 974441
          |}
        """.stripMargin.getBytes

      val message = CreateRabbitMessage("tag", envelope, properties, body, BatchQueue)

      consumer ! StartConsume
      consumer ! message

      val expectedMessage = RabbitMessageContact(RabbitMetadata(BatchQueue, "Contact", 1L), body)
      probe.expectMsg(expectedMessage)
    }
  }

  "A consumer" must {
    "stop consume and stop rabbit connection and channels" in {
      val probe = TestProbe()
      val batchProbe = TestProbe()
      val liveProbe = TestProbe()
      val connection = TestProbe()
      val consumer = TestActorRef(Props(new ConsumerSupervisor {
        override def consuming(rabbitConnection: ActorRef, batch: ActorRef, live: ActorRef): Receive = super.consuming(connection.ref, batchProbe.ref, liveProbe.ref)
      }))

      probe.watch(connection.ref)
      probe.watch(batchProbe.ref)
      probe.watch(liveProbe.ref)

      consumer ! StartConsume
      consumer ! StopConsume

      probe.expectTerminated(connection.ref)
      probe.expectTerminated(batchProbe.ref)
      probe.expectTerminated(liveProbe.ref)
    }
  }

  "A consumer" must {
    "process acknowledge properly by queue type" in {
      val batchProbe = TestProbe()
      val liveProbe = TestProbe()
      val connection = TestProbe()
      val consumer = TestActorRef(Props(new ConsumerSupervisor {
        override def consuming(rabbitConnection: ActorRef, batch: ActorRef, live: ActorRef): Receive = super.consuming(connection.ref, batchProbe.ref, liveProbe.ref)
      }))

      consumer ! StartConsume

      consumer ! ProcessAck(BatchQueue, 1L)
      batchProbe.expectMsgType[ChannelMessage]
      liveProbe.expectNoMsg()

      consumer ! ProcessAck(LiveQueue, 1L)
      liveProbe.expectMsgType[ChannelMessage]
      batchProbe.expectNoMsg()
    }
  }
}
