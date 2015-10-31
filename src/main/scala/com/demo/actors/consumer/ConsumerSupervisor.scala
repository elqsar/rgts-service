package com.demo.actors.consumer

import akka.actor.SupervisorStrategy._
import akka.actor._
import com.demo.actors.processor.ProcessorSupervisor
import com.demo.configuration.Configuration
import com.demo.messages.Messages._
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, Envelope}
import com.thenewmotion.akka.rabbitmq._

import scala.concurrent.duration._

class ConsumerSupervisor extends Actor with ActorLogging {
  val connectionFactory = Configuration.connectionFactory()
  val liveQueueName = Configuration.liveQueueName()
  val batchQueueName = Configuration.batchQueueName()
  val rabbitFetchSize = Configuration.prefetchSize()

  val processorSupervisor = context.actorOf(ProcessorSupervisor.props(), ProcessorSupervisor.name)

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 5, withinTimeRange = 60.seconds) {
    case _: Exception => Restart
  }

  override def receive: Receive = waiting()

  def consuming(rabbitConnection: ActorRef, batch: ActorRef, live: ActorRef): Receive = {
    case StopConsume =>
      context.stop(rabbitConnection)
      context.stop(batch)
      context.stop(live)
      context.unbecome()

    case ProcessAck(queueType, deliveryTag) =>
      def publishAck(channel: Channel) = channel.basicAck(deliveryTag, false)
      queueType match {
        case BatchQueue => batch ! ChannelMessage(publishAck, dropIfNoChannel = false)
        case LiveQueue => live ! ChannelMessage(publishAck, dropIfNoChannel = false)
      }

    case CreateRabbitMessage(_, envelope, properties, body, queueType) =>
      val rabbitMessage = createRabbitMessage(envelope, properties, body, queueType)
      processorSupervisor ! rabbitMessage
  }

  def waiting(): Receive = {
    case StartConsume =>
      val rabbitConnection = context.actorOf(ConnectionActor.props(connectionFactory).withDispatcher("consumer"), "rabbitConnection")
      val batch = rabbitConnection.createChannel(ChannelActor.props(setupConsumer(batchQueueName, BatchQueue, self)).withDispatcher("rabbit"), Some("batchConsumer"))
      val live = rabbitConnection.createChannel(ChannelActor.props(setupConsumer(liveQueueName, LiveQueue, self)).withDispatcher("rabbit"), Some("liveConsumer"))

      context.become(consuming(rabbitConnection, batch, live))
  }

  def setupConsumer(queueName: String, queueType: RabbitQueue, supervisor: ActorRef): (Channel, ActorRef) => Any = {
    def setupSubscriber(channel: Channel, self: ActorRef) = {
      val consumer = new DefaultConsumer(channel) {
        override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]): Unit = {
          supervisor ! CreateRabbitMessage(consumerTag, envelope, properties, body, queueType)
        }
      }
      channel.basicQos(rabbitFetchSize)
      channel.basicConsume(queueName, false, consumer)
    }
    setupSubscriber
  }

  def createRabbitMessage(envelope: Envelope, properties: BasicProperties, body: Array[Byte], queueType: RabbitQueue): RabbitMessage = {
    val mediaType = properties.getHeaders.getOrDefault("x-cision-record-type", "Unknown").toString
    val deliveryTag = envelope.getDeliveryTag
    val metadata = RabbitMetadata(queueType, mediaType, deliveryTag)

    log.info("Message type is: {}", mediaType)
    mediaType match {
      case "Contact" => RabbitMessageContact(metadata, body)
      case "Outlet" => RabbitMessageOutlet(metadata, body)
    }
  }
}

object ConsumerSupervisor {
  val name = "consumerSupervisor"

  def props() = Props(new ConsumerSupervisor)
}
