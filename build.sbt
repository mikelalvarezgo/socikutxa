ThisBuild / scalaVersion     := "2.13.17"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
      name := "BardenasKutxa",
      libraryDependencies ++= Dependencies.commonDependencies
  )

enablePlugins(FlywayPlugin)
flywayDriver   := "org.postgresql.Driver"
flywayUrl      := "jdbc:postgresql://localhost:5432/bardenas"
flywayUser     := "admin"
flywayPassword := "12345"
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
