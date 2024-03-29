package dev.yann.bookingengine.room

import spray.json.DefaultJsonProtocol.*
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import java.time.LocalDateTime
import dev.yann.bookingengine.extension.JsonFormatExtension.localDateTimeFormat

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


