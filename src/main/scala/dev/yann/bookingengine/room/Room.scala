package dev.yann.bookingengine.room

import dev.yann.bookingengine.extension.JsonFormatExtension.uuidJsonFormat
import dev.yann.bookingengine.room.RoomRoute.jsonFormat5
import dev.yann.bookingengine.setting.ApplicationConfig.dbSchema
import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery
import spray.json.DefaultJsonProtocol.{IntJsonFormat, StringJsonFormat}
import spray.json.RootJsonFormat

import java.util.UUID

final case class Room(id: UUID, number: String, name: String, capacity: Int, location: String)

lazy val roomTable = TableQuery[RoomTable]

class RoomTable(tag: Tag) extends Table[Room](tag, Some(dbSchema), "ROOM"):
  val id = column[UUID]("ID", O.PrimaryKey)
  val number = column[String]("NUMBER")
  val name = column[String]("NAME")
  val capacity = column[Int]("CAPACITY")
  val location = column[String]("LOCATION")

  override def * = (id, number, name, capacity, location).mapTo[Room]

given reservationFormat: RootJsonFormat[Room] = jsonFormat5(Room.apply)
