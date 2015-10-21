package com.demo.actors.breaker

import akka.actor.Scheduler
import akka.pattern.CircuitBreaker

import scala.concurrent.duration._

trait EndpointGuard {
  import scala.concurrent.ExecutionContext.Implicits.global

  def createBreaker(scheduler: Scheduler): CircuitBreaker = {
    val breaker = new CircuitBreaker(
      scheduler,
      maxFailures = 2,
      callTimeout = 20.seconds,
      resetTimeout = 25.seconds
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
