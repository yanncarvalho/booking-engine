package dev.yann.bookingengine.setting

import dev.yann.bookingengine.setting.ApplicationConfig.dbSchema

class Migration:

  import ApplicationConfig.{dbPassword, dbUser, jdbcUrl}
  import org.flywaydb.core.Flyway
  import org.flywaydb.core.api.output.MigrateResult

  def startupDb: MigrateResult =

    val flyway: Flyway = Flyway
      .configure()
      .dataSource(
        jdbcUrl,
        dbUser,
        dbPassword
      ).validateOnMigrate(false).load()

    flyway.migrate()
