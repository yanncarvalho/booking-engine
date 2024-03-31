package dev.yann.bookingengine

import akka.actor.ActorSystem
import dev.yann.bookingengine.route.Route
import dev.yann.bookingengine.setting.{DatabaseMigration, Server}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContextExecutor

@main def boot(): Unit =

  given system: ActorSystem                        = ActorSystem("BookingHotel")
  given executionContext: ExecutionContextExecutor = system.dispatcher
  given logger: Logger = LoggerFactory.getLogger(classOf[main])

  DatabaseMigration().startup
  Server(Route.route).startup
