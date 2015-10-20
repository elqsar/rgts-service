package com.demo.messages

import com.demo.domain.{MojoContact, Contact}

object Messages {
  case object BreakMe
  case class RabbitMessage(mediaType: String, body: Array[Byte])
  case class RdsReadyContact(contact: Contact)
  case class RdsReadyOutlet(outlet: Any)
  case class MojoReadyContact(contact: MojoContact)
  case class MojoReadyOutlet(outlet: Any)
  case class PostRequest(contact: MojoContact)
}
