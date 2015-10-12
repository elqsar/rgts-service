package com.demo.messages

object Messages {
  case object BreakMe
  case class RabbitMessage(body: Array[Byte])
  case class ProcessedMessage[T](content: T)
}
