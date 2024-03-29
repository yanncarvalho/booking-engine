package dev.yann.bookingengine.reservation

import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives.validate
import dev.yann.bookingengine.reservation.ReservationDTO.AddReservation

import ReservationDTO.AddReservation
import dev.yann.bookingengine.room.RoomDTO.AddRoom

object ReservationDirective:

  def validateAddReservation(add: AddReservation): Directive0 =
    val checkin  = add.checkin.getOrElse(add.estimatedCheckin.orNull)
    val checkout = add.checkout.getOrElse(add.estimatedCheckout.orNull)
    val (isValid, message) =
      if checkin == null || checkout == null then
        (
          false,
          "The 'checkin' or 'estimatedCheckin' and 'checkout' or 'estimatedCheckout' variables must be provided"
        )
      else if !checkin.isBefore(checkout) then
        (false, "Checkin must be before checkout")
      else if add.clientCpf.matches("\\d{11}") then
        (false, "CPF must consist of 11 digits only")
      else (true, "")
    validate(isValid, message)
