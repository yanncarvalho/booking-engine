package dev.yann.bookingengine.reservation

import slick.jdbc.PostgresProfile.api.*
import slick.lifted.TableQuery
import spray.json.DefaultJsonProtocol.*
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import java.time.LocalDateTime
import dev.yann.bookingengine.extension.JsonFormatExtension.given
import dev.yann.bookingengine.setting.ApplicationConfig.dbSchema

import java.util.UUID

final case class Reservation(
    id: UUID,
    roomId: UUID,
    estimatedCheckout: LocalDateTime,
    estimatedCheckin: LocalDateTime,
    checkout: Option[LocalDateTime],
    checkin: Option[LocalDateTime],
    clientCpf: String
)

lazy val reservationTable = TableQuery[ReservationTable]
class ReservationTable(tag: Tag)
    extends Table[Reservation](tag, Some(dbSchema), "RESERVATION"):
  val id                = column[UUID]("ID", O.PrimaryKey)
  val roomId            = column[UUID]("ROOM_ID")
  val estimatedCheckout = column[LocalDateTime]("ESTIMATED_CHECKOUT")
  val estimatedCheckin  = column[LocalDateTime]("ESTIMATED_CHECKIN")
  val checkout          = column[Option[LocalDateTime]]("CHECKOUT")
  val checkin           = column[Option[LocalDateTime]]("CHECKIN")
  val clientCpf         = column[String]("CLIENT_CPF")
  override def * = (
    id,
    roomId,
    estimatedCheckout,
    estimatedCheckin,
    checkout,
    checkin,
    clientCpf
  ).mapTo[Reservation]

given reservationFormat: RootJsonFormat[Reservation] = jsonFormat7(
  Reservation.apply
)
