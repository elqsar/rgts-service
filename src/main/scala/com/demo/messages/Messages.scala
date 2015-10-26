package com.demo.messages

import com.demo.actors.consumer.ConsumerSupervisor.RabbitQueue
import com.demo.domain.{MojoContact, Contact}
import com.ning.http.client.Response
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Envelope

object Messages {
  case class RabbitMessage(metadata: RabbitMetadata, body: Array[Byte])
  case class RdsReadyContact(metadata: RabbitMetadata, contact: Contact)
  case class MojoReadyContact(contact: MojoContact)
  case class PostRequest(metadata: RabbitMetadata, contact: MojoContact)
  case class CreateRabbitMessage(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte], queueType: RabbitQueue)

  case class ProcessAck(queue: RabbitQueue, deliveryTag: Long)

  case class SuccessResponse(metadata: RabbitMetadata, response: Response)

  case class RabbitMetadata(queueType: RabbitQueue, mediaType: String, deliveryTag: Long)
}
