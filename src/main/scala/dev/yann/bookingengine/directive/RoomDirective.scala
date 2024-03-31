package dev.yann.bookingengine.directive

import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.Directives.validate
import dev.yann.bookingengine.dto.RoomDTO.AddRoom

object RoomDirective:

  def validateAddRoom(addRoom: AddRoom): Directive0 =

    val (isValid, message) =
      if addRoom.name.isBlank then (false, "The room name must be provided")
      else if !addRoom.number.matches("^([a-zA-Z]|\\d)(.*)$") then
        (false, "Number must start with a number or a letter")
      else (true, "")
    validate(isValid, message)
