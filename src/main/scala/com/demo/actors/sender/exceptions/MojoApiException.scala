package com.demo.actors.sender.exceptions

case class MojoApiException(message: String) extends RuntimeException(message)