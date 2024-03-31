package dev.yann.bookingengine.extension

import dev.yann.bookingengine.setting.ApplicationConfig
import dev.yann.bookingengine.setting.ApplicationConfig.{localDateFormat, localDateTimeFormat}
import spray.json.{DeserializationException, JsString, JsValue, JsonFormat}

import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import akka.http.scaladsl.unmarshalling.FromStringUnmarshaller
import akka.http.scaladsl.unmarshalling.Unmarshaller

import java.time.LocalDate
object MarshallUnmarshallExtension:
  
  lazy private val dateTimeFormat =
    DateTimeFormatter.ofPattern(localDateTimeFormat)
  
  lazy private val dateFormat = DateTimeFormatter.ofPattern(localDateFormat)


  given localDateTimeJson: JsonFormat[LocalDateTime] with
    override def write(obj: LocalDateTime): JsValue =
      JsString(obj.format(dateTimeFormat))

    override def read(json: JsValue): LocalDateTime =
      json match
        case JsString(value) => LocalDateTime.parse(value, dateTimeFormat)
        case _ => throw DeserializationException("LocalDateTime expected")
  
  given uuidJson: JsonFormat[java.util.UUID] with
    override def write(uuid: java.util.UUID): JsValue = JsString(uuid.toString)

    override def read(json: JsValue): java.util.UUID = json match
      case JsString(value) => java.util.UUID.fromString(value)
      case _               => throw DeserializationException("UUID expected")

  given localDateUnmarshaller: FromStringUnmarshaller[LocalDate] =
    Unmarshaller.strict[String, LocalDate](LocalDate.parse(_, dateFormat))