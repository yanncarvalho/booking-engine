package dev.yann.bookingengine

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import com.typesafe.config.{Config, ConfigFactory}
import dev.yann.bookingengine.reservation.ReservationRepository.getByDate
import dev.yann.bookingengine.room.RoomRepository
import dev.yann.bookingengine.route.Routes
import dev.yann.bookingengine.setting.Migration

import java.time.LocalDate
import scala.concurrent.ExecutionContextExecutor


@main def main: Unit =
  given system: ActorSystem = ActorSystem("BookingHotel")
  given executionContext: ExecutionContextExecutor = system.dispatcher

  val server = Http().newServerAt("localhost", 8080).bind(Routes.route)

  Migration().startupDb
  val logger = LoggerFactory.getLogger(classOf[main])
  server.map { _ =>
    logger.info("Successfully started on localhost:8080")
  } recover {
    case ex =>
      logger.error("Failed to start the server due to: {}", ex.getMessage)
  }
