package com.demo.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.demo.messages.Messages
import com.demo.messages.Messages.BreakMe
import Messages._
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, ConnectionFactory, DefaultConsumer, Envelope}
import com.thenewmotion.akka.rabbitmq.{ChannelActor, ConnectionActor, CreateChannel}

class Receptionist(processorSupervisor: ActorRef) extends Actor with ActorLogging {
  val connectionFactory = new ConnectionFactory()
  connectionFactory.setHost("localhost")

  val queueName = "test.queue"

  val rabbitConnection = context.actorOf(ConnectionActor.props(connectionFactory), "rabbitConnection")

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info("preStart() of Receptionist")
    setUpConsumer()
  }

  def setUpConsumer() = {
    def setupSubscriber(channel: Channel, self: ActorRef) = {
      val consumer = new DefaultConsumer(channel) {
        override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]): Unit = {
          processorSupervisor ! RabbitMessage(body)
        }
      }
      channel.basicConsume(queueName, true, consumer)
    }

    rabbitConnection ! CreateChannel(ChannelActor.props(setupSubscriber), Some("subscriber"))
  }

  override def receive: Receive = {
    case BreakMe => println("Hello")
  }
}

object Receptionist {
  def props(processorSupervisor: ActorRef) = Props(new Receptionist(processorSupervisor))
}
