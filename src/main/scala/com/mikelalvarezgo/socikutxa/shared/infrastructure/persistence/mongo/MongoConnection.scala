package com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.mongo

import org.mongodb.scala.{MongoClient, MongoCollection}
import org.mongodb.scala.bson.Document

final class MongoConnection(config: MongoConfig) {

  private val client: MongoClient = MongoClient(config.settings)

  val db = client.getDatabase("socikutxa")

  def getCollection(collection: String): MongoCollection[Document] =
    db.getCollection(collection)
}
