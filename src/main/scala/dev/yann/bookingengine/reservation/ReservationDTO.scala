package dev.yann.bookingengine.reservation

import dev.yann.bookingengine.extension.JsonFormatExtension.localDateTimeFormat
import spray.json.DefaultJsonProtocol.*
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import java.time.LocalDateTime
import java.util.UUID

import dev.yann.bookingengine.extension.JsonFormatExtension.uuidJsonFormat

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
