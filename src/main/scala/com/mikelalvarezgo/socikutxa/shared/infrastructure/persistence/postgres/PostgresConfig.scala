package com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.postgres

import cats.effect.IO
import com.typesafe.config.Config
import doobie.Transactor
final case class PostgresConfig(host: String, port: Int, user: String, password: String) {
  private val dbName = "bardenas"
  private val url    = s"jdbc:postgres://$host:$port/$dbName"

  val transactor: Transactor[IO] = Transactor.fromDriverManager[IO](
      driver = "org.postgresql.Driver",
      url = url,
      user = user,
      password = password,
      logHandler = None
  )
}
object PostgresConfig                                                                    {
  def fromConfig(config: Config): PostgresConfig =
    PostgresConfig(
        config.getString("host"),
        config.getInt("port"),
        config.getString("user"),
        config.getString("password")
    )
}
