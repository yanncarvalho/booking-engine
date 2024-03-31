package dev.yann.bookingengine.repository

import dev.yann.bookingengine.dto.RoomDTO.AddRoom
import dev.yann.bookingengine.model.{Room, roomTable}
import dev.yann.bookingengine.setting.Database

import java.util.UUID
import scala.collection.Seq
import scala.concurrent.Future

class RoomRepository:
  private val db = Database.db
  implicit val ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global
  import slick.jdbc.PostgresProfile.api.*
  def getAll: Future[Seq[Room]] = db.run {
    roomTable.to[Seq].result
  }

  def add(add: AddRoom): Future[Room] = db.run {
    roomTable returning roomTable += add.toRoom
  }

  def remove(id: UUID): Future[Int] = db.run {
    roomTable.filter(_.id === id).delete
  }
