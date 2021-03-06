package com.demo.configuration

import com.rabbitmq.client.ConnectionFactory
import com.typesafe.config.ConfigFactory

object Configuration {
  val config = ConfigFactory.load()

  def batchQueueName() = config.getString("queue.batch.name")

  def liveQueueName() = config.getString("queue.live.name")

  def apiBaseUrl() = config.getString("api.baseUrl")

  def prefetchSize() = config.getInt("rabbit.qos")

  def connectionFactory(): ConnectionFactory = {
    val connectionFactory = new ConnectionFactory()
    connectionFactory.setHost(config.getString("rabbit.connection.host"))
    connectionFactory
  }
}
