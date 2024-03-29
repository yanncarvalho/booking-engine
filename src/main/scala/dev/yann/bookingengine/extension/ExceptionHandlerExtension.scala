package dev.yann.bookingengine.extension

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.{ExceptionHandler, RejectionHandler}
import spray.json.DefaultJsonProtocol.*
import spray.json.{DefaultJsonProtocol, DeserializationException, RootJsonFormat}

object ExceptionHandlerExtension extends SprayJsonSupport :
  private given exceptionHttpFormat: RootJsonFormat[ExceptionHttp] =
    jsonFormat2(ExceptionHttp.apply)
  private final case class ExceptionHttp(httpStatus: Int, error: String)
  given rejectionHandler: RejectionHandler = RejectionHandler.newBuilder()
    .handle {
      case ex =>
        complete(ExceptionHttp(
          StatusCodes.Conflict.intValue,
          "Bad numbers, bad result!!!"+ex
        ))
      // Add more rejection cases as needed
    }
    .result()


  given exceptionHandler: ExceptionHandler = ExceptionHandler {
    case ex: DeserializationException =>
      complete(
        ExceptionHttp(
          StatusCodes.Conflict.intValue,
          "Bad numbers, bad result!!!"
        )
      )
    case _: IllegalArgumentException =>
      complete(StatusCodes.BadRequest,  ExceptionHttp(
        StatusCodes.Conflict.intValue,
        "Bad numbers, bad result!!!"
      ))

    case _: Exception =>
      complete(StatusCodes.BadRequest, ExceptionHttp(
        StatusCodes.Conflict.intValue,
        "Bad numbers, bad result!!!"
      ))


  }
