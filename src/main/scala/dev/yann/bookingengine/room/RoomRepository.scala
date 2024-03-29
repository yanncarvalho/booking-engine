package dev.yann.bookingengine.room

import dev.yann.bookingengine.room.RoomDTO.AddRoom
import dev.yann.bookingengine.setting.Database

import scala.collection.Seq
import scala.concurrent.Future


import java.util.UUID

object RoomRepository:
  private val db = Database.db

  import slick.jdbc.PostgresProfile.api.*
  def getAll: Future[Seq[Room]] = db.run{
    roomTable.to[Seq].result
  }

  def getByNumber(number: String): Future[Option[Room]] = db.run{
    roomTable.filter(_.number === number).result.headOption
  }

  def add(add: AddRoom): Future[Room]  = db.run {
    roomTable returning roomTable += add.toRoom
  }

  def remove(id: UUID): Future[Int]  = db.run {
    roomTable.filter(_.id === id).delete
  }



