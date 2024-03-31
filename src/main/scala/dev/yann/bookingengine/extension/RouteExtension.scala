package dev.yann.bookingengine.extension

import scala.concurrent.duration.{Duration, SECONDS}
import scala.concurrent.{Await, Future}

trait RouteExtension:
  def getResult[T](f: Future[T]): T =
    Await.result(f, Duration(10, SECONDS))
