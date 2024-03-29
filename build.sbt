ThisBuild / version := "1.0"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "BookingEngine"
  )

resolvers += "Akka library repository".at("https://repo.akka.io/maven")
val AkkaVersion     = "2.9.0"
val AkkaHttpVersion = "10.6.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka"  %% "akka-http-spray-json"       % AkkaHttpVersion,
  "com.typesafe.akka"  %% "akka-actor"                 % AkkaVersion,
  "com.typesafe.akka"  %% "akka-stream"                % AkkaVersion,
  "com.typesafe.akka"  %% "akka-http"                  % AkkaHttpVersion,
  "org.scalatest"      %% "scalatest"                  % "3.2.17"  % "test",
  "org.postgresql"      % "postgresql"                 % "42.7.3",
  "com.typesafe.slick" %% "slick"                      % "3.5.0",
  "com.typesafe.slick" %% "slick-hikaricp"             % "3.5.0",
  "com.typesafe.slick" %% "slick-codegen"              % "3.5.0",
  "org.flywaydb"        % "flyway-core"                % "10.10.0",
  "org.slf4j"           % "slf4j-api"                  % "2.0.12",
  "ch.qos.logback"      % "logback-classic"            % "1.5.3",
  "com.typesafe"        % "config"                     % "1.4.3",
  "org.flywaydb"        % "flyway-database-postgresql" % "10.10.0" % "runtime"
)
