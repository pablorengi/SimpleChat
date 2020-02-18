package com.server

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import akka.pattern.{ask, pipe}

import scala.concurrent.duration._
import com.server.Server.{Connect, Disconnect, Message}

object Client {
  def props(server: ActorRef) = Props(new Client(server))
}

class Client(server: ActorRef) extends Actor {
  import context.dispatcher

  implicit val timeout = Timeout(5 seconds)

  override def preStart= {
    println(self.path)
    server ! Connect
  }

  def receive: Receive = {
    case Disconnect =>
      (server ? Disconnect).pipeTo(self)
    case Disconnect =>
      context.stop(self)
    case body : String =>
      server ! Message(self, body)
    case msg : Message =>
      println(s"Message from [${msg.author}] at [${msg.timeStamp}]: ${msg.body}")
  }
}
