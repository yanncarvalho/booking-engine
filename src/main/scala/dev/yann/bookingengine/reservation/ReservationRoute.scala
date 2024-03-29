package dev.yann.bookingengine.reservation

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{Directives, ExceptionHandler, Route}
import dev.yann.bookingengine.reservation.ReservationDTO.AddReservation
import spray.json.*
import spray.json.DefaultJsonProtocol.*

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import ReservationDTO.AddReservation
import dev.yann.bookingengine.extension.RouteExtension
import dev.yann.bookingengine.setting.ApplicationConfig.dateFormat
import dev.yann.bookingengine.setting.ApplicationConfig

import java.util.UUID

object ReservationRoute
    extends SprayJsonSupport
    with DefaultJsonProtocol
    with RouteExtension:
  def apply(): Route =
    pathPrefix("reservation") {
      path("getByDate") {
        get {
          parameter("date") { (dateStr: String) =>

              val date = LocalDate.parse(
                dateStr,
                DateTimeFormatter.ofPattern(dateFormat)
              )
              complete(getResult(ReservationRepository.getByDate(date)).toJson)

          }
        }
      } ~ path("add") {
        post {
          entity(as[AddReservation]) { add =>
              val result = getResult(ReservationRepository.add(add))
              result match
                case None =>
                  complete(
                    StatusCodes.Conflict -> "{\"Fail\": \"Room is not available\"}"
                  )
                case Some(r) => complete(r)


          }
        }
      } ~ path("remove") {
        parameter("id") { (id: String) =>
      
            complete(getResult(ReservationRepository.remove(UUID.fromString(id))).toJson)
          
        }
      }
    }
