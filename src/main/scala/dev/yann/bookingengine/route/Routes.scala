package dev.yann.bookingengine.route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import dev.yann.bookingengine.extension.ExceptionHandlerExtension.given
import dev.yann.bookingengine.reservation.ReservationRoute
import dev.yann.bookingengine.room.RoomRoute

import scala.language.postfixOps


object Routes:

  val route: Route = handleRejections(rejectionHandler)(Route.seal(
    ReservationRoute() ~ RoomRoute()
  ))

