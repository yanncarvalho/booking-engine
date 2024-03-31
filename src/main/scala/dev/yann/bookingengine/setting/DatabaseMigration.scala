package dev.yann.bookingengine.setting

class DatabaseMigration:

  import ApplicationConfig.{dbPassword, dbUser, jdbcUrl}
  import org.flywaydb.core.Flyway
  import org.flywaydb.core.api.output.MigrateResult

  def startup: MigrateResult =

    val flyway: Flyway = Flyway
      .configure()
      .dataSource(
        jdbcUrl,
        dbUser,
        dbPassword
      )
      .validateOnMigrate(false)
      .load()

    flyway.migrate()
