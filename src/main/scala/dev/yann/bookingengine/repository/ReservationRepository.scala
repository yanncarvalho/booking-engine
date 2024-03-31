package dev.yann.bookingengine.repository

import dev.yann.bookingengine.dto.ReservationDTO.AddReservation
import dev.yann.bookingengine.model.{Reservation, reservationTable, roomTable}
import dev.yann.bookingengine.setting.Database
import slick.lifted.CanBeQueryCondition

import java.time.{LocalDate, LocalDateTime}
import java.util.UUID
import scala.concurrent.ExecutionContext.global
import scala.concurrent.Future

class ReservationRepository:

  private val db = Database.db

  import slick.jdbc.PostgresProfile.api.*

  given ec: scala.concurrent.ExecutionContext =
    scala.concurrent.ExecutionContext.global

  def getByDate(date: LocalDate): Future[Seq[Reservation]] = db.run {
    val currDay = date.atTime(23, 59, 59)

    reservationTable.filter { e =>
      e.checkin.isDefined && (e.checkout.isEmpty || e.checkout >= currDay)
    }.result
  }

  def add(book: AddReservation): Future[Reservation] = db.run {

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
              reservationTable returning reservationTable += book.toReservation
            case None => DBIO.successful(null)
          }
        case _ => DBIO.successful(null)
      }
  }

  def remove(id: UUID): Future[Int] = db.run {
    reservationTable.filter(_.id === id).delete
  }
