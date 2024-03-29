package dev.yann.bookingengine.room

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.*
import akka.http.scaladsl.server.Directives.*
import dev.yann.bookingengine.extension.RouteExtension
import dev.yann.bookingengine.reservation.ReservationRoute.getResult
import dev.yann.bookingengine.room.RoomDTO.AddRoom
import dev.yann.bookingengine.room.RoomDirective.validateAddRoom
import spray.json.*
import spray.json.DefaultJsonProtocol.*

import java.util.UUID

object RoomRoute
  extends SprayJsonSupport
    with DefaultJsonProtocol
    with RouteExtension:

  def apply(): Route =
    pathPrefix("room") {
      path("getAll") {
        get {
          val result = getResult(RoomRepository.getAll)
          result match
            case Nil => reject
            case _ => complete(result.toJson)

        }
      } ~ path("getByNumber") {
        parameter("number") { (number: String) =>
          get {
            val result = getResult(RoomRepository.getByNumber(number))
            result match
              case None => reject
              case Some(r) => complete(r)
          }
        }
      } ~ path("add") {
        post {

          entity(as[AddRoom]) { add =>
            validateAddRoom(add) {
              complete(getResult(RoomRepository.add(add)).toJson)
            }
          }
        }
      }
    } ~ path("remove") {
      delete {
        parameter("id") { (id: String) =>

            val result = getResult(RoomRepository.remove(UUID.fromString(id)))
            if result > 0 then
              complete(s"{\"Success\": \"id removed successfully\"}")
            else
              reject
          }

      }
    }
