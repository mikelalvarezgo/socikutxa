package com.mikelalvarezgo.socikutxa.shared.infrastructure.configuration

import com.typesafe.config.{Config, ConfigFactory}

case class DatabaseConfig(
    driver: String,
    url: String,
    user: String,
    password: String
)

case class MongoConfig(
    host: String,
    port: Int,
    user: String,
    password: String
)

case class ServerConfig(
    host: String,
    port: Int
)

case class AppConfig(
    database: DatabaseConfig,
    mongo: MongoConfig,
    server: ServerConfig,
    typeSafeConfig: Config
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

    val mongo = MongoConfig(
        host = config.getString("mongo.host"),
        port = config.getInt("mongo.port"),
        user = config.getString("mongo.user"),
        password = config.getString("mongo.password")
    )

    val server = ServerConfig(
        host = config.getString("server.host"),
        port = config.getInt("server.port")
    )

    AppConfig(database, mongo, server, config)
  }
}
