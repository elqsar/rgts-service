package com.demo.actors.breaker

import java.util.concurrent.Executor

import akka.actor.Scheduler
import akka.pattern.CircuitBreaker

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait EndpointGuard {

  def createCircuitBreaker(scheduler: Scheduler)(implicit exec: ExecutionContext): CircuitBreaker = {
    new CircuitBreaker(
      scheduler,
      maxFailures = 10,
      callTimeout = 1.minute,
      resetTimeout = 30.seconds
    )
      .onOpen(notifyCircuitBreakerOpen())
      .onHalfOpen(notifyCircuitBreakerHalfOpen())
      .onClose(notifyCircuitBreakerClose())
  }

  def notifyCircuitBreakerOpen(): Unit

  def notifyCircuitBreakerHalfOpen(): Unit

  def notifyCircuitBreakerClose(): Unit
}
