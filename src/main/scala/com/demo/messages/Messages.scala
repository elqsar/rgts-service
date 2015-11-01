package com.demo.messages

import com.demo.domain.cision.{Outlet, Contact}
import com.demo.domain.mojo.{MojoOutlet, MojoContact}
import com.ning.http.client.Response
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client.Envelope
import org.json4s._

object Messages {
  trait RabbitMessage
  case class RabbitMessageContact(metadata: RabbitMetadata, rawContact: Array[Byte]) extends RabbitMessage
  case class RabbitMessageOutlet(metadata: RabbitMetadata, rawOutlet: Array[Byte]) extends RabbitMessage
  case class CreateRabbitMessage(consumerTag: String, envelope: Envelope, properties: BasicProperties, body: Array[Byte], queueType: RabbitQueue)
  case class ProcessAck(queue: RabbitQueue, deliveryTag: Long)
  case class ProcessNack(queue: RabbitQueue, deliveryTag: Long, requeue: Boolean)
  case class RabbitMetadata(queueType: RabbitQueue, mediaType: String, deliveryTag: Long)

  case class ProcessContact(metadata: RabbitMetadata, content: JValue)
  case class ProcessOutlet(metadata: RabbitMetadata, content: JValue)

  case class RdsReadyContact(metadata: RabbitMetadata, contact: Contact)
  case class RdsReadyOutlet(metadata: RabbitMetadata, outlet: Outlet)

  case class MojoReadyContact(contact: MojoContact)
  case class MojoReadyOutlet(outlet: Outlet)

  case class GetOutletRequest(id: Long, metadata: RabbitMetadata)
  case class GetContactRequest(id: Long, metadata: RabbitMetadata)

  case class EncodeContact(metadata: RabbitMetadata, contact: MojoContact)
  case class EncodeOutlet(metadata: RabbitMetadata, outlet: MojoOutlet)

  case class PostContactRequest(id: Long, metadata: RabbitMetadata, contact: String)
  case class PostOutletRequest(id: Long, metadata: RabbitMetadata, outlet: String)
  case class PutContactRequest(id: Long, metadata: RabbitMetadata, outlet: String)
  case class PutOutletRequest(id: Long, metadata: RabbitMetadata, outlet: String)

  case class SuccessResponse(metadata: RabbitMetadata, response: Response)
  case class FailedResponse(metadata: RabbitMetadata, response: Response)

  case object CheckHealth

  case object StopConsume
  case object StartConsume

  trait RabbitQueue
  case object BatchQueue extends RabbitQueue
  case object LiveQueue extends RabbitQueue

  case class FailureMessage(errorMessage: String, failure: Failure, message: Option[Any])

  trait Failure
  case object UnknownMediaType extends Failure
  case object RdsDecodeFailure extends Failure
  case object RdsMappingFailure extends Failure
  case object MojoParseFailure extends Failure
  case object MojoMappingFailure extends Failure
  case object MojoEncodeFailure extends Failure
  case object MojoCreateFailure extends Failure
  case object MojoUpdateFailure extends Failure
}
