package dev.yann.bookingengine.route

import akka.http.scaladsl.common.ToNameReceptacleEnhancements._string2NR
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives.{_enhanceRouteWithConcatenation, as, entity, get, parameter, path, pathPrefix, post}
import akka.http.scaladsl.server.directives.ParameterDirectives.ParamSpec
import akka.http.scaladsl.server.directives.RouteDirectives
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{Directives, Route}
import dev.yann.bookingengine.directive.ReservationDirective.validateAddReservation
import dev.yann.bookingengine.dto.ReservationDTO.AddReservation
import dev.yann.bookingengine.extension.HttpHandlerExtension.{BadRequestException, HttpResponse, NotFoundException}
import dev.yann.bookingengine.extension.MarshallUnmarshallExtension.given
import dev.yann.bookingengine.extension.RouteExtension
import dev.yann.bookingengine.model.given
import dev.yann.bookingengine.repository.ReservationRepository
import spray.json.DefaultJsonProtocol

import java.time.LocalDate
import java.util.UUID
object ReservationRoute
    extends SprayJsonSupport
    with DefaultJsonProtocol
    with RouteExtension
    with RouteDirectives:
  private val repository: ReservationRepository = ReservationRepository()

  def apply(): Route =
    pathPrefix("reservation") {
      path("getByDate") {
        get {

          parameter("date".as[LocalDate]) { date =>

            val reservationList =
              getResult(repository.getByDate(date))

            if reservationList.isEmpty then
              throw NotFoundException("Not found any reservation")
            complete(HttpResponse(entity = reservationList))

          }
        }
      } ~ path("add") {

        post {

          entity(as[AddReservation]) { (add: AddReservation) =>
            validateAddReservation(add) {
              val reservation = getResult(repository.add(add))
              if reservation == null then
                throw BadRequestException("Room is not available")
              complete(HttpResponse(entity = reservation))
            }
          }
        }
      } ~ path("remove") {
        parameter("id".as[UUID]) { (id: UUID) =>

          val numRowsDeleted = getResult(repository.remove(id))
          if numRowsDeleted <= 0 then
            throw NotFoundException("Reservation not found")

          complete(HttpResponse(entity = "id removed successfully"))

        }
      }
    }
