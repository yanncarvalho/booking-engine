package dev.yann.bookingengine.dto

import dev.yann.bookingengine.model.Room
import dev.yann.bookingengine.route.ReservationRoute.StringJsonFormat
import dev.yann.bookingengine.route.RoomRoute.StringJsonFormat
import spray.json.DefaultJsonProtocol.{IntJsonFormat, StringJsonFormat, jsonFormat4}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import java.util.UUID

object RoomDTO:
  given addRoomFormat: RootJsonFormat[AddRoom] = jsonFormat4 {
    AddRoom.apply
  }

  final case class AddRoom(
      number: String,
      name: String,
      capacity: Int,
      location: String
  ):
    def toRoom: Room = Room(UUID.randomUUID, number, name, capacity, location)
