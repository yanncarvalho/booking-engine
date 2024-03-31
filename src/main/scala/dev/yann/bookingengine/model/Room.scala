package dev.yann.bookingengine.model

import dev.yann.bookingengine.extension.MarshallUnmarshallExtension.{uuidJson, given}
import dev.yann.bookingengine.model.{Reservation, Room}
import dev.yann.bookingengine.route.RoomRoute.jsonFormat5
import dev.yann.bookingengine.setting.ApplicationConfig.dbSchema
import slick.jdbc.PostgresProfile.api.{Table, TableQuery, Tag, given}
import slick.lifted.TableQuery
import spray.json.DefaultJsonProtocol.{IntJsonFormat, StringJsonFormat, jsonFormat7, optionFormat}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import java.time.LocalDateTime
import java.util.UUID

given roomFormat: RootJsonFormat[Room] = jsonFormat5(Room.apply)
final case class Room(
    id: UUID,
    number: String,
    name: String,
    capacity: Int,
    location: String
)

lazy val roomTable = TableQuery[RoomTable]
class RoomTable(tag: Tag) extends Table[Room](tag, Some(dbSchema), "ROOM"):
  val id       = column[UUID]("ID", O.PrimaryKey)
  val number   = column[String]("NUMBER")
  val name     = column[String]("NAME")
  val capacity = column[Int]("CAPACITY")
  val location = column[String]("LOCATION")

  override def * = (id, number, name, capacity, location).mapTo[Room]


