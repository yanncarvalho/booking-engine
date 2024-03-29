package dev.yann.bookingengine.reservation

import dev.yann.bookingengine.reservation.ReservationDTO.AddReservation
import slick.lifted.CanBeQueryCondition

import java.time.{LocalDate, LocalDateTime}
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future
import ReservationDTO.AddReservation
import dev.yann.bookingengine.setting.Database
import dev.yann.bookingengine.room.roomTable

import java.util.UUID

object ReservationRepository:

  private val db = Database.db

  import slick.jdbc.PostgresProfile.api.*

  given ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  def getByDate(date: LocalDate): Future[Seq[Reservation]] = db.run {
    val currDay = date.atTime(23, 59, 59)

    reservationTable.filter { e =>
      e.checkin.isDefined && (e.checkout.isEmpty || e.checkout <= currDay)
    }.result
  }

  def add(book: AddReservation): Future[Option[Reservation]] = db.run {

    val checkout = book.checkout.getOrElse(book.estimatedCheckout.get)
    val checkin  = book.checkin.getOrElse(book.estimatedCheckin.get)
    reservationTable
      .filter { e =>
        val checkoutQuery: Rep[LocalDateTime] =
          e.checkout.getOrElse(e.estimatedCheckout)
        val checkinQuery: Rep[LocalDateTime] =
          e.checkin.getOrElse(e.estimatedCheckin)
        e.roomId === book.roomId
        && ((checkinQuery <= checkin && checkoutQuery >= checkin.minusHours(4))
          || (checkinQuery > checkin && checkinQuery <= checkout.plusHours(4)))
      }
      .result
      .flatMap {
        case Nil =>
          roomTable.filter(_.id === book.roomId).result.headOption.flatMap {
            case Some(_) =>
              val newReservation = book.toReservation
              (reservationTable returning reservationTable)
                .insertOrUpdate(newReservation)
                .map(_ => Option(newReservation))
            case None => DBIO.successful(Option.empty[Reservation])
          }
        case _ => DBIO.successful(Option.empty[Reservation])
      }
  }

  def remove(id: UUID): Future[Int] = db.run {
    reservationTable.filter(_.id === id).delete
  }
