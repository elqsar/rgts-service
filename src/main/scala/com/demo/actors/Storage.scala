package com.demo.actors

import akka.actor.{Actor, ActorLogging}

class Storage extends Actor with ActorLogging {
  override def receive: Receive = {
    case message =>

  }
}

object Storage {

}
