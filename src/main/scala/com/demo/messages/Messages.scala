package com.demo.messages


import com.demo.actors.consumer.ConsumerSupervisor.RabbitQueue
import com.demo.domain.cision.{Outlet, Contact}
import com.demo.domain.mojo.MojoContact
import com.ning.http.client.Response
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Envelope

object Messages {
  case class RabbitMessage(metadata: RabbitMetadata, body: Array[Byte])
  case class CreateRabbitMessage(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte], queueType: RabbitQueue)
  case class ProcessAck(queue: RabbitQueue, deliveryTag: Long)
  case class RabbitMetadata(queueType: RabbitQueue, mediaType: String, deliveryTag: Long)

  case class RdsReadyContact(metadata: RabbitMetadata, contact: Contact)
  case class RdsReadyOutlet(metadata: RabbitMetadata, outlet: Outlet)

  case class MojoReadyContact(contact: MojoContact)
  case class MojoReadyOutlet(outlet: Outlet)

  case class GetOutletRequest(id: Long, metadata: RabbitMetadata)
  case class GetContactRequest(id: Long, metadata: RabbitMetadata)

  case class PostContactRequest(metadata: RabbitMetadata, contact: MojoContact)
  case class PostOutletRequest(metadata: RabbitMetadata, outlet: Outlet)

  case class SuccessResponse(metadata: RabbitMetadata, response: Response)
}
