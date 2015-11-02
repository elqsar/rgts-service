package com.demo.actors.endpoint.exceptions

case class MojoApiException(message: String) extends RuntimeException(message)