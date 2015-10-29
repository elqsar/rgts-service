package com.demo.actors.breaker

import akka.actor.Scheduler
import akka.pattern.CircuitBreaker

import scala.concurrent.duration._

trait EndpointGuard {
  import scala.concurrent.ExecutionContext.Implicits.global

  def createCircuitBreaker(scheduler: Scheduler): CircuitBreaker = {
    val breaker = new CircuitBreaker(
      scheduler,
      maxFailures = 10,
      callTimeout = 1.minute,
      resetTimeout = 30.seconds
    )
      .onOpen(notifyCircuitBreakerOpen())
      .onHalfOpen(notifyCircuitBreakerHalfOpen())
      .onClose(notifyCircuitBreakerClose())

    breaker
  }

  def notifyCircuitBreakerOpen(): Unit

  def notifyCircuitBreakerHalfOpen(): Unit

  def notifyCircuitBreakerClose(): Unit
}
