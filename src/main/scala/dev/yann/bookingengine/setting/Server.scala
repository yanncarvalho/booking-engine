package dev.yann.bookingengine.setting

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import dev.yann.bookingengine.setting.ApplicationConfig.{httpHost, httpPort}
import org.slf4j.Logger

import scala.concurrent.ExecutionContextExecutor;

class Server(route: Route):

  def startup(using
      system: ActorSystem,
      executionContext: ExecutionContextExecutor,
      logger: Logger
  ): Unit =
    val server = Http().newServerAt(httpHost, httpPort).bind(route)

    server.map { _ =>
      logger.info(s"Successfully started on $httpHost:$httpPort")
    } recover { case ex =>
      logger.error("Failed to start the server due to: {}", ex.getMessage)
    }
