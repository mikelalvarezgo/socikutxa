package com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.mongo

import akka.util.ccompat.JavaConverters.ListHasAsScala
import org.bson.BsonDateTime
import org.joda.time.DateTime
import org.mongodb.scala.Document
import org.mongodb.scala.bson.{BsonArray, BsonInt32, BsonObjectId, BsonString}

object MongoUtils {

  final private class ValueNotFoundException(field: String) extends Throwable {
    override def getMessage: String = s"Value $field not found in mongo document"
  }

  implicit class MongoDocumentOps(value: Document) {
    def getOptString(key: String): Option[String]                                =
      value.get[BsonString](key).map(_.getValue)
    def getListString(key: String): Seq[String]                                  =
      getValue(
          value
            .get[BsonArray](key)
            .map(_.getValues.asScala.toList.map(_.asString().getValue)),
          key
      )
    def getStr(key: String): String                                              = getValue(getOptString(key), key)
    def getOptInt(key: String): Option[Int]                                      = value.get[BsonInt32](key).map(_.getValue)
    def getInt(key: String): Int                                                 = getValue(getOptInt(key), key)
    def getOptDate(key: String): Option[DateTime]                                =
      value
        .get[BsonDateTime](key)
        .map(_.getValue)
        .map(
            millis => new DateTime(millis)
        )
    def getDate(key: String): DateTime                                           = getValue(getOptDate(key), key)
    def getOptId(key: String): Option[String]                                    =
      value.get[BsonObjectId](key).map(_.asString().getValue)
    def getId(key: String): String                                               = getValue(getOptId(key), key)
    def getOptValues[T](
        key: String
    )(implicit converter: MongoConverter[T]): Option[Seq[T]]                     =
      value
        .get[BsonArray](key)
        .map(
            _.getValues.asScala.toList.map(
                doc => converter.toValue(doc.asDocument())
            )
        )
    def getValues[T](key: String)(implicit converter: MongoConverter[T]): Seq[T] =
      getValue(getOptValues(key), key)

    private def getValue[S](optValue: Option[S], fieldName: String): S =
      optValue.getOrElse(throw new ValueNotFoundException(fieldName))
  }
}
