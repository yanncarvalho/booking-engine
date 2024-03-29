package dev.yann.bookingengine.setting

import com.typesafe.config.ConfigFactory;

object ApplicationConfig:
  private lazy val config = ConfigFactory.load()
  private lazy val httpConfig = config.getConfig("http")
  private lazy val slickConfig = config.getConfig("database.properties")
  private lazy val appConfig = config.getConfig("application")

  lazy val applicationName: String = config.getString("application.name")
  lazy val httpHost: String = httpConfig.getString("interface")
  lazy val httpPort: Int = httpConfig.getInt("port")

  lazy val dateFormat: String = appConfig.getString("defaultDateFormat")
  lazy val dateTimeFormat: String = appConfig.getString("defaultDateTimeFormat")

  lazy val dbSchema: String = config.getString("database.dbSchema")
  lazy val serverName: String = slickConfig.getString("serverName")
  lazy val dbPort: String = slickConfig.getString("portNumber")
  lazy val dbName: String = slickConfig.getString("name")
  lazy val jdbcUrl: String = slickConfig.getString("url")
  lazy val dbUser: String = slickConfig.getString("user")
  lazy val dbPassword: String = slickConfig.getString("password")


