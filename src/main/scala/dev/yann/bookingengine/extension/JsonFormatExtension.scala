package dev.yann.bookingengine.extension

import dev.yann.bookingengine.setting.ApplicationConfig
import dev.yann.bookingengine.setting.ApplicationConfig.dateTimeFormat
import spray.json.{DeserializationException, JsString, JsValue, JsonFormat}

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.{Try, Success, Failure}

object JsonFormatExtension:
  private val format = DateTimeFormatter.ofPattern(dateTimeFormat)

  given localDateTimeFormat: JsonFormat[LocalDateTime] with
    override def write(obj: LocalDateTime): JsValue =
      JsString(obj.format(format))

    override def read(json: JsValue): LocalDateTime =
      json match
        case JsString(value) => LocalDateTime.parse(value, format)
        case _ => throw DeserializationException("LocalDateTime expected")

  given uuidJsonFormat: JsonFormat[java.util.UUID] with
    override def write(uuid: java.util.UUID): JsValue = JsString(uuid.toString)

    override def read(json: JsValue): java.util.UUID = json match
      case JsString(value) => java.util.UUID.fromString(value)
      case _ => throw DeserializationException("UUID expected")


