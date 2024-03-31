package dev.yann.bookingengine.dto

import dev.yann.bookingengine.extension.MarshallUnmarshallExtension.{localDateTimeJson, uuidJson}
import dev.yann.bookingengine.model.Reservation
import dev.yann.bookingengine.route.ReservationRoute.StringJsonFormat
import dev.yann.bookingengine.route.RoomRoute.StringJsonFormat
import spray.json.DefaultJsonProtocol.{StringJsonFormat, jsonFormat6, optionFormat}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import java.time.LocalDateTime
import java.util.UUID

object ReservationDTO:
  given addReservationFormat: RootJsonFormat[AddReservation] = jsonFormat6(
    AddReservation.apply
  )

  final case class AddReservation(
      roomId: UUID,
      estimatedCheckout: Option[LocalDateTime],
      estimatedCheckin: Option[LocalDateTime],
      checkout: Option[LocalDateTime],
      checkin: Option[LocalDateTime],
      clientCpf: String
  ):
    def toReservation: Reservation = Reservation(
      UUID.randomUUID(),
      roomId,
      estimatedCheckout.getOrElse(checkout.getOrElse(LocalDateTime.now)),
      estimatedCheckin.getOrElse(checkout.getOrElse(LocalDateTime.now)),
      checkout,
      checkin,
      clientCpf
    )
