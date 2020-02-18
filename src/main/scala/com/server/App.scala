package com.server

import akka.actor.{ActorRef, ActorSystem}

import scala.concurrent.duration._

object ClientApp extends App {

  val actorSystem = ActorSystem("Server")

  implicit val dispatcher = actorSystem.dispatcher

  val serverAddress = "akka.tcp://Server@127.0.0.1:2552/user/server"

  actorSystem.actorSelection(serverAddress).resolveOne(3 seconds).onComplete({
    case server : ActorRef =>
      val client = actorSystem.actorOf(Client.props(server), "client")
      actorSystem.actorOf(ClientInterface.props(client), "clientInterface")
  })
}

object ServerApp extends App {

  val actorSystem = ActorSystem("Server")

  println(actorSystem.actorOf(Server.props, "server").path)

}
