package com.mikelalvarezgo.socikutxa.shared.infrastructure.configuration

import com.typesafe.config.ConfigFactory

case class DatabaseConfig(
    driver: String,
    url: String,
    user: String,
    password: String
)

case class ServerConfig(
    host: String,
    port: Int
)

case class AppConfig(
    database: DatabaseConfig,
    server: ServerConfig
)

object AppConfig {
  def load(): AppConfig = {
    val config = ConfigFactory.load()

    val database = DatabaseConfig(
        driver = config.getString("database.driver"),
        url = config.getString("database.url"),
        user = config.getString("database.user"),
        password = config.getString("database.password")
    )

    val server = ServerConfig(
        host = config.getString("server.host"),
        port = config.getInt("server.port")
    )

    AppConfig(database, server)
  }
}
