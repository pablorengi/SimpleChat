package com.server

import akka.actor.{Actor, ActorRef, Props}
import com.server.Server.{Disconnect}
import scala.io.StdIn.readLine

object ClientInterface {
  case object Check

  def props(chatClient: ActorRef) = Props(new ClientInterface(chatClient))
}

class ClientInterface(client: ActorRef) extends Actor {
  import ClientInterface._

  override def preStart() = {
    println("You are now logged in. Press enter to send. Type 'exit' to log out.")
    self ! Check
  }

  def receive = {
    case Check =>
      readLine() match {
        case "exit" =>
          client ! Disconnect
          println("Disconnecting...")
          context.stop(self)
        case msg : String =>
          client ! msg
          self ! Check
      }
  }
}
