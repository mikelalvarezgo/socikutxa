import sbt.*

object Dependencies {
  object Version {
    val akka            = "2.6.18"
    val akkaHttp        = "10.2.7"
    val akkaHttpCirce   = "1.37.0"
    val akkaHttpCors    = "1.1.2"
    val catsEffect        = "3.5.7"
    val catsCore        = "2.6.1"
    val circe           = "0.14.1"
    val joda            = "2.10.13"
    val kamon           = "2.2.3"
    val kamonHttp4s     = "2.2.1"
    val mockito         = "1.16.46"
    val mongo = "5.1.1"
    val doobie = "1.0.0-RC4"
    val scalalikeJdbc = "4.3.0"
    val reactiveMongo = "1.0.3"
    val scalaLogging    = "3.9.4"
    val typeSafe    = "1.4.1"
    val scalaTest       = "3.2.10"
    val sealerate       = "0.0.6"
    val slf4j           = "1.7.30"
  }

  object General {

    val circeExtras       = circe("circe-generic-extras")
    val circeGeneric      = circe("circe-generic")
    val circeParser       = circe("circe-parser")
    val circeLiteral      = circe("circe-literal")

    val akkaActor = "com.typesafe.akka" %% "akka-actor"             % Version.akka
    val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j"             % Version.akka
    val akkaStream = "com.typesafe.akka" %% "akka-stream"             % Version.akka
    val akkaHttp          = "com.typesafe.akka" %% "akka-http"             % Version.akkaHttp
    val akkaHttpCirce     = "de.heikoseeberger" %% "akka-http-circe"       % Version.akkaHttpCirce
    val akkaHttpCors      = "ch.megard"         %% "akka-http-cors"        % Version.akkaHttpCors
    val cats              = "org.typelevel"     %% "cats-core"             % Version.catsCore
    val catsEffect = "org.typelevel" %% "cats-effect" % Version.catsEffect
    val typeSafe          = "com.typesafe"       % "config"                % Version.typeSafe

    val joda = "joda-time" % "joda-time" % Version.joda
    // Enumeration
    val sealarate = "ca.mrvisser" %% "sealerate" % Version.sealerate
    // Reporters
    val kamonBundle        = kamon("kamon-bundle")
    val kamonSystemMetrics = kamon("kamon-system-metrics")

    val mongoDriver = "org.mongodb.scala" %% "mongo-scala-driver" % Version.mongo
    // Start with this one
    val doobie = "org.tpolecat" %% "doobie-core" % Version.doobie
    val scalalikeJdbc = "org.scalikejdbc" %% "scalikejdbc" % Version.scalalikeJdbc
    val postgresql =   "org.postgresql" % "postgresql" % "42.7.3"   // PostgreSQL JDBC driver
    // Logs
    val scalaLogging    = "com.typesafe.scala-logging" %% "scala-logging"            % Version.scalaLogging

  }

  object Testing {
    val scalaTest         = "org.scalatest"     %% "scalatest"         % Version.scalaTest
    val mockito           = "org.mockito"       %% "mockito-scala"     % Version.mockito
  }

  val commonDependencies: Seq[sbt.ModuleID] = Seq(
    General.akkaActor,
    General.akkaStream,
    General.akkaSlf4j,
    General.akkaHttp,
    General.akkaHttpCirce,
    General.akkaHttpCors,
    General.cats,
    General.catsEffect,
    General.circeExtras,
    General.circeGeneric,
    General.circeLiteral,
    General.kamonBundle,
    General.kamonSystemMetrics,
    General.mongoDriver,
    General.typeSafe,
    General.scalaLogging,
    General.postgresql,
    General.doobie,
    General.joda,
    General.sealarate,
    Testing.mockito,
    Testing.scalaTest
  )


  protected def circe(artifact: String): ModuleID    = "io.circe"              %% artifact % Version.circe
  protected def kamon(artifact: String): ModuleID    = "io.kamon"              %% artifact % Version.kamon
}
