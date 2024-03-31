package dev.yann.bookingengine.extension

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.{ExceptionHandler, MalformedQueryParamRejection, RejectionHandler, ValidationRejection}
import spray.json.DefaultJsonProtocol.{IntJsonFormat, jsonFormat2}
import spray.json.{DefaultJsonProtocol, JsonFormat, RootJsonFormat}

import java.time.format.DateTimeParseException

object HttpHandlerExtension extends SprayJsonSupport with DefaultJsonProtocol:

  given httpResponseFormat[K: JsonFormat]: RootJsonFormat[HttpResponse[K]] =
    jsonFormat2(HttpResponse.apply[K])

  final case class HttpResponse[K](httpStatus: Int = 200, entity: K)

  final case class NotFoundException(message: String = "Not found element")
      extends RuntimeException(message)

  final case class BadRequestException(message: String)
      extends RuntimeException(message)

  final case class InternalServerErrorException(message: String)
      extends RuntimeException(message)

  object RejectionHandlerExtension:

    given rejectionHandler: RejectionHandler = RejectionHandler
      .newBuilder()
      .handle {

        case ex: ValidationRejection =>
          complete(
            HttpResponse(
              StatusCodes.Conflict.intValue,
              ex.message
            )
          )
        case ex: MalformedQueryParamRejection =>
          complete(
            StatusCodes.BadRequest,
            HttpResponse(
              StatusCodes.BadRequest.intValue,
              ex.errorMsg
            )
          )

        case _ =>
          complete(
            HttpResponse(
              StatusCodes.InternalServerError.intValue,
              "Unable to process"
            )
          )
      }
      .result()

  object ExceptionHandlerExtension:
    given exceptionHandler: ExceptionHandler = ExceptionHandler {

      case ex: InternalServerErrorException =>
        complete(
          StatusCodes.InternalServerError,
          HttpResponse(
            StatusCodes.InternalServerError.intValue,
            ex.message
          )
        )

      case ex: (BadRequestException | DateTimeParseException) =>
        complete(
          StatusCodes.BadRequest,
          HttpResponse(
            StatusCodes.BadRequest.intValue,
            ex.getMessage
          )
        )

      case ex: NotFoundException =>
        complete(
          StatusCodes.NotFound,
          HttpResponse(
            StatusCodes.NotFound.intValue,
            ex.message
          )
        )

      case ex: Exception =>
        complete(
          StatusCodes.InternalServerError,
          HttpResponse(
            StatusCodes.InternalServerError.intValue,
            ex.getMessage
          )
        )

    }
