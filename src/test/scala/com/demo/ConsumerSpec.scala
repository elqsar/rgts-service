package com.demo

import java.util

import akka.actor.{Terminated, ActorRef, Props, ActorSystem}
import akka.testkit.{TestActorRef, TestProbe, ImplicitSender, TestKit}
import com.demo.actors.consumer.ConsumerSupervisor
import com.demo.actors.consumer.ConsumerSupervisor._
import com.demo.messages.Messages.{ProcessAck, RabbitMetadata, RabbitMessage, CreateRabbitMessage}
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, Envelope}
import com.thenewmotion.akka.rabbitmq.ChannelMessage
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

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
      val map = new util.HashMap[String, Object]()
      map.put("x-cision-record-type", "Contact")
      properties.setHeaders(map)
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

      val expectedMessage = RabbitMessage(RabbitMetadata(BatchQueue, "Contact", 1L), body)
      probe.expectMsg(expectedMessage)
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
