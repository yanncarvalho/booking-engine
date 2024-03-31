package dev.yann.bookingengine.route

import akka.http.scaladsl.server.Directives.{_enhanceRouteWithConcatenation, handleRejections}
import akka.http.scaladsl.server.Route
import dev.yann.bookingengine.extension.HttpHandlerExtension.RejectionHandlerExtension.rejectionHandler
import dev.yann.bookingengine.extension.HttpHandlerExtension.ExceptionHandlerExtension.given

import scala.language.postfixOps

object Route:

  val route: Route = handleRejections(rejectionHandler)(
    akka.http.scaladsl.server.Route.seal(
      ReservationRoute() ~ RoomRoute()
    )
  )
