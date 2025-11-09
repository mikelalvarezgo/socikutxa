package com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.mongo

import com.mongodb.client.{MongoClient, MongoClients}
import io.mongock.runner.standalone.MongockStandalone

class MongockRunner(config: MongoConfig) {

  def runMigrations(): Unit = {
    val client: MongoClient = MongoClients.create(config.url)

    try {
      MongockStandalone
        .builder()
        .setDriver(io.mongock.driver.mongodb.sync.v4.driver.MongoSync4Driver.withDefaultLock(client, "bardenas"))
        .addMigrationScanPackage("com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.mongo.migrations")
        .setTransactionEnabled(false)
        .buildRunner()
        .execute()

      println("Mongock migrations completed successfully")
    } finally {
      client.close()
    }
  }
}
