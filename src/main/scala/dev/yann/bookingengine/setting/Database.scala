package dev.yann.bookingengine.setting

object Database:
  val db = slick.jdbc.PostgresProfile.api.Database.forConfig("database")
