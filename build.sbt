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

// MongoDB migration task
lazy val mongoMigrate = taskKey[Unit]("Run MongoDB migrations using mongosh")
mongoMigrate := {
  import scala.sys.process._
  val migrationsDir = baseDirectory.value / "migrations"
  val migrations = (migrationsDir * "*.js").get.sorted

  println("Running MongoDB migrations...")
  migrations.foreach { migration =>
    println(s"Executing ${migration.name}...")
    val exitCode = s"docker exec mongo_db mongosh -u admin -p 12345 --authenticationDatabase admin --file /tmp/${migration.name}".!
    if (exitCode != 0) {
      throw new RuntimeException(s"Migration ${migration.name} failed with exit code $exitCode")
    }
  }
  println("All migrations completed successfully")
}

lazy val mongoMigrateDocker = taskKey[Unit]("Copy migrations to Docker and run them")
mongoMigrateDocker := {
  import scala.sys.process._
  val migrationsDir = baseDirectory.value / "migrations"
  val migrations = (migrationsDir * "*.js").get.sorted

  println("Copying migrations to Docker container...")
  migrations.foreach { migration =>
    val exitCode = s"docker cp ${migration.absolutePath} mongo_db:/tmp/${migration.name}".!
    if (exitCode != 0) {
      throw new RuntimeException(s"Failed to copy ${migration.name} to Docker container")
    }
  }

  println("Running MongoDB migrations in Docker...")
  migrations.foreach { migration =>
    println(s"Executing ${migration.name}...")
    val exitCode = s"docker exec mongo_db mongosh -u admin -p 12345 --authenticationDatabase admin --file /tmp/${migration.name}".!
    if (exitCode != 0) {
      throw new RuntimeException(s"Migration ${migration.name} failed with exit code $exitCode")
    }
  }
  println("All migrations completed successfully")
}

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
