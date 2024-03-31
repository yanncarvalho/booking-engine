package dev.yann.bookingengine.route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives.{_enhanceRouteWithConcatenation, as, complete, delete, entity, get, parameter, path, pathPrefix, post}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.PathDirectives._string2NR
import dev.yann.bookingengine.directive.RoomDirective.validateAddRoom
import dev.yann.bookingengine.dto.RoomDTO.AddRoom
import dev.yann.bookingengine.extension.HttpHandlerExtension.{BadRequestException, HttpResponse, NotFoundException, given}
import dev.yann.bookingengine.extension.RouteExtension
import dev.yann.bookingengine.model.given
import dev.yann.bookingengine.repository.RoomRepository
import dev.yann.bookingengine.route.ReservationRoute.getResult
import spray.json.DefaultJsonProtocol

import java.util.UUID

object RoomRoute
    extends SprayJsonSupport
    with DefaultJsonProtocol
    with RouteExtension:
  private val repository: RoomRepository = RoomRepository()
  def apply(): Route =
    pathPrefix("room") {

      path("getAll") {
        get {

          val rooms = getResult(repository.getAll)
          if rooms.isEmpty then throw NotFoundException("Not found any room")
          complete(HttpResponse(entity = rooms))

        }
      } ~ path("add") {
        post {

          entity(as[AddRoom]) { (add: AddRoom) =>
            validateAddRoom(add) {
              val room = getResult(repository.add(add))
              if room == null then
                throw BadRequestException("Unable to create the room")
              complete(HttpResponse(entity = room))

            }
          }
        }
      } ~ path("remove") {
        delete {
          parameter("id".as[UUID]) { (id: UUID) =>
            val numRowsDeleted = getResult(repository.remove(id))
            if numRowsDeleted <= 0 then
              throw BadRequestException("Unable to remove the room")
            complete(HttpResponse(entity = "id removed successfully"))
          }
        }
      }
    }
