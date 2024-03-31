ThisBuild / version := "1.0"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "BookingEngine"
  )

val AkkaVersion     = "2.8.5"
val AkkaHttpVersion = "10.5.3"
val SlickVersion    = "3.5.0"
val FlywayVersion   = "10.10.0"
val PostgresVersion = "42.7.2"

val logAndConfigDependencies = Seq(
  "com.typesafe"   % "config"          % "1.4.3",
  "org.slf4j"      % "slf4j-api"       % "2.0.9",
  "ch.qos.logback" % "logback-classic" % "1.4.14"
)

val akkaDependencies = Seq(
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor"           % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream"          % AkkaVersion,
  "com.typesafe.akka" %% "akka-http"            % AkkaHttpVersion
)

val databaseDependencies = Seq(
  "com.typesafe.slick" %% "slick"          % SlickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
  "org.flywaydb"        % "flyway-core"    % FlywayVersion,
    "org.postgresql"      % "postgresql"     % PostgresVersion,
  "org.flywaydb" % "flyway-database-postgresql" % FlywayVersion % "runtime"
)

libraryDependencies ++= logAndConfigDependencies ++ akkaDependencies ++ databaseDependencies
