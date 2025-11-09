package com.mikelalvarezgo.socikutxa

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.semigroupk._
import com.comcast.ip4s._
import com.mikelalvarezgo.socikutxa.product.infrastructure.ProductContext
import com.mikelalvarezgo.socikutxa.user.infrastructure.UserContext
import com.mikelalvarezgo.socikutxa.shared.infrastructure.configuration.AppConfig
import com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.mongo.MongockRunner
import doobie.Transactor
import org.http4s.Response.http4sKleisliResponseSyntaxOptionT
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.Router

object App extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val config = AppConfig.load()

    // Run MongoDB migrations
    val mongoConfig = com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.mongo.MongoConfig.fromConfig(
      config.typeSafeConfig.getConfig("mongo")
    )
    val mongockRunner = new MongockRunner(mongoConfig)
    mongockRunner.runMigrations()

    val transactor: Transactor[IO] = Transactor.fromDriverManager[IO](
        driver = config.database.driver,
        url = config.database.url,
        user = config.database.user,
        password = config.database.password,
        logHandler = None
    )

    val productContext = new ProductContext(transactor)
    val userContext    = new UserContext(transactor)

    val combinedRoutes = productContext.routes <+> userContext.routes

    val routes = Router(
        "/api" -> combinedRoutes
    ).orNotFound

    val host = Ipv4Address.fromString(config.server.host).getOrElse(ipv4"0.0.0.0")
    val port = Port.fromInt(config.server.port).getOrElse(port"8080")

    EmberServerBuilder
      .default[IO]
      .withHost(host)
      .withPort(port)
      .withHttpApp(routes)
      .build
      .use(
          _ => IO.never
      )
      .as(ExitCode.Success)
  }
}
