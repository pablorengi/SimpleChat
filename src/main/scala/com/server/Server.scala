package com.server

import akka.actor.{Actor, ActorRef, Props, Terminated}

object Server {
  case object Connect
  case object Disconnect
  case object Disconnected
  case class Message(author: ActorRef, body: String, timeStamp: Long = System.currentTimeMillis())

  def props = Props(new Server())
}

class Server extends Actor {

  import Server._

  var onlineClients = Set.empty[ActorRef]

  def receive = {
    case Connect =>
      onlineClients += sender
      context.watch(sender)
    case Disconnect =>
      onlineClients -= sender
      context.unwatch(sender)
      sender ! Disconnected
    case Terminated(ref) =>
      onlineClients -= ref
    case msg: Message =>
      onlineClients.filter(_ != sender).foreach(_ ! msg)
  }
}