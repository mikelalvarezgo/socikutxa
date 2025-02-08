package com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.mongo

import org.mongodb.scala.MongoClient

abstract class MongoCollectionHelper {

  implicit val client: MongoClient

  val collectionName: String

  def createCollection(): Unit = {
    client.getDatabase("socikutxa").createCollection(collectionName)
    ()
  }

  def deleteCollection(): Unit = {
    client.getDatabase("socikutxa").getCollection(collectionName).drop()
    ()
  }
}
