package com.demo.actors.consumer

import akka.actor.SupervisorStrategy._
import akka.actor._
import com.demo.actors.consumer.ConsumerSupervisor._
import com.demo.actors.processor.ProcessorSupervisor
import com.demo.configuration.Configuration
import com.demo.messages.Messages.{CreateRabbitMessage, RabbitMetadata, BreakMe, RabbitMessage}
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, Envelope}
import com.thenewmotion.akka.rabbitmq._

import scala.concurrent.duration._

class ConsumerSupervisor extends Actor with ActorLogging {
  val connectionFactory = Configuration.connectionFactory()
  val liveQueueName = Configuration.liveQueueName()
  val batchQueueName = Configuration.batchQueueName()

  val processorSupervisor = context.actorOf(ProcessorSupervisor.props(), "processorSupervisor")

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 5, withinTimeRange = 60.seconds) {
    case _: Exception => Restart
  }

  def setupConsumer(queueName: String, queueType: RabbitQueue, supervisor: ActorRef): (Channel, ActorRef) => Any = {
    def setupSubscriber(channel: Channel, self: ActorRef) = {
      val consumer = new DefaultConsumer(channel) {
        override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]): Unit = {
          supervisor ! CreateRabbitMessage(consumerTag, envelope, properties, body, queueType)
        }
      }
      channel.basicConsume(queueName, false, consumer)
    }
    setupSubscriber
  }

  def createRabbitMessage(envelope: Envelope, properties: BasicProperties, body: Array[Byte], queueType: RabbitQueue): RabbitMessage = {
    val mediaType = properties.getHeaders.getOrDefault("x-cision-record-type", "Unknown").toString
    val deliveryTag = envelope.getDeliveryTag
    RabbitMessage(RabbitMetadata(queueType, mediaType, deliveryTag), body)
  }

  override def receive: Receive = waiting()

  def consuming(rabbitConnection: ActorRef, batch: ActorRef, live: ActorRef): Receive = {
    case StopConsume =>
      context.stop(rabbitConnection)
      context.unbecome()

    case ProcessAck(queueType, deliveryTag) =>
      def publishAck(channel: Channel) = channel.basicAck(deliveryTag, false)
      queueType match {
        case BatchQueue => batch ! ChannelMessage(publishAck)
        case LiveQueue => live ! ChannelMessage(publishAck)
      }

    case CreateRabbitMessage(_, envelope, properties, body, queueType) =>
      val rabbitMessage = createRabbitMessage(envelope, properties, body, queueType)
      processorSupervisor ! rabbitMessage
  }

  def waiting(): Receive = {
    case StartConsume =>
      val rabbitConnection = context.actorOf(ConnectionActor.props(connectionFactory), "rabbitConnection")
      val batch = rabbitConnection.createChannel(ChannelActor.props(setupConsumer(batchQueueName, BatchQueue, self)), Some("batchConsumer"))
      val live = rabbitConnection.createChannel(ChannelActor.props(setupConsumer(liveQueueName, LiveQueue, self)), Some("liveConsumer"))

      context.become(consuming(rabbitConnection, batch, live))
  }
}

object ConsumerSupervisor {
  case object StopConsume
  case object StartConsume
  case class ProcessAck(queue: RabbitQueue, deliveryTag: Long)

  trait RabbitQueue
  case object BatchQueue extends RabbitQueue
  case object LiveQueue extends RabbitQueue

  def props() = Props(new ConsumerSupervisor)
}