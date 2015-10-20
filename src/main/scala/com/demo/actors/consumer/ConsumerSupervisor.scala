package com.demo.actors.consumer

import akka.actor.SupervisorStrategy._
import akka.actor._
import com.demo.actors.processor.ProcessorSupervisor
import com.demo.configuration.Configuration
import com.demo.messages.Messages.{BreakMe, RabbitMessage}
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.{Channel, Envelope}
import com.thenewmotion.akka.rabbitmq._

import scala.concurrent.duration._

class ConsumerSupervisor extends Actor with ActorLogging {
  val connectionFactory = Configuration.connectionFactory()
  val liveQueueName = Configuration.liveQueueName()
  val batchQueueName = Configuration.batchQueueName()

  val rabbitConnection = context.actorOf(ConnectionActor.props(connectionFactory), "rabbitConnection")
  val processorSupervisor = context.actorOf(ProcessorSupervisor.props(), "processorSupervisor")

  rabbitConnection ! CreateChannel(ChannelActor.props(setupConsumer(batchQueueName)), Some("batchConsumer"))
  rabbitConnection ! CreateChannel(ChannelActor.props(setupConsumer(liveQueueName)), Some("liveConsumer"))

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(
    maxNrOfRetries = 5, withinTimeRange = 60.seconds) {
    case _: Exception => Restart
  }

  def setupConsumer(queueName: String): (Channel, ActorRef) => Any = {
    def setupSubscriber(channel: Channel, self: ActorRef) = {
      val consumer = new DefaultConsumer(channel) {
        override def handleDelivery(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte]): Unit = {
          val rabbitMessage = createRabbitMessage(properties, body)
          processorSupervisor ! rabbitMessage
          channel.basicAck(envelope.getDeliveryTag, false)
        }
      }
      channel.basicConsume(queueName, false, consumer)
    }
    setupSubscriber
  }

  def createRabbitMessage(properties: BasicProperties, body: Array[Byte]): RabbitMessage = {
    val mediaType = properties.getHeaders.getOrDefault("x-cision-record-type", "Unknown").toString
    log.info("Media type: {}", mediaType.toString)
    RabbitMessage(mediaType, body)
  }

  override def receive: Receive = {
    case BreakMe =>
  }
}

object ConsumerSupervisor {
  def props() = Props(new ConsumerSupervisor)
}
