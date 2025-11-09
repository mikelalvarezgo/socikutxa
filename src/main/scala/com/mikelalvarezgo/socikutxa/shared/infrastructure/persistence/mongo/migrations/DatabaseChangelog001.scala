package com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.mongo.migrations

import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.{IndexOptions, Indexes}
import io.mongock.api.annotations.{ChangeUnit, Execution, RollbackExecution}
import org.bson.Document

@ChangeUnit(id = "create-orders-collection", order = "001", author = "bardenas")
class DatabaseChangelog001 {

  @Execution
  def createOrdersCollection(database: MongoDatabase): Unit = {
    // Create the orders collection
    database.createCollection("orders")

    val ordersCollection = database.getCollection("orders")

    // Create indexes
    ordersCollection.createIndex(
      Indexes.ascending("buyer.id"),
      new IndexOptions().name("idx_orders_buyer_id")
    )

    ordersCollection.createIndex(
      Indexes.ascending("seller.id"),
      new IndexOptions().name("idx_orders_seller_id")
    )

    ordersCollection.createIndex(
      Indexes.descending("createdAt"),
      new IndexOptions().name("idx_orders_createdAt")
    )

    ordersCollection.createIndex(
      Indexes.ascending("status"),
      new IndexOptions().name("idx_orders_status")
    )

    // Add validation schema
    val validationRule = Document.parse(
      """
        {
          "$jsonSchema": {
            "bsonType": "object",
            "required": ["_id", "buyer", "seller", "products", "status", "createdAt"],
            "properties": {
              "_id": {
                "bsonType": "string",
                "description": "Order ID - required"
              },
              "buyer": {
                "bsonType": "object",
                "required": ["id", "email", "fullName"],
                "properties": {
                  "id": {
                    "bsonType": "objectId",
                    "description": "Buyer ID - required"
                  },
                  "email": {
                    "bsonType": "string",
                    "description": "Buyer email - required"
                  },
                  "fullName": {
                    "bsonType": "string",
                    "description": "Buyer full name - required"
                  }
                }
              },
              "seller": {
                "bsonType": "object",
                "required": ["id", "email", "fullName"],
                "properties": {
                  "id": {
                    "bsonType": "objectId",
                    "description": "Seller ID - required"
                  },
                  "email": {
                    "bsonType": "string",
                    "description": "Seller email - required"
                  },
                  "fullName": {
                    "bsonType": "string",
                    "description": "Seller full name - required"
                  }
                }
              },
              "products": {
                "bsonType": "array",
                "minItems": 1,
                "items": {
                  "bsonType": "object",
                  "required": ["id", "name", "price", "quantity"],
                  "properties": {
                    "id": {
                      "bsonType": "objectId",
                      "description": "Product ID - required"
                    },
                    "name": {
                      "bsonType": "string",
                      "description": "Product name - required"
                    },
                    "price": {
                      "bsonType": "double",
                      "minimum": 0,
                      "description": "Product price - required"
                    },
                    "quantity": {
                      "bsonType": "int",
                      "minimum": 1,
                      "description": "Product quantity - required"
                    }
                  }
                },
                "description": "Order products - required"
              },
              "status": {
                "bsonType": "string",
                "enum": ["DRAFT", "IN_PROGRESS", "COMPLETED", "CANCELLED"],
                "description": "Order status - required"
              },
              "createdAt": {
                "bsonType": "date",
                "description": "Creation timestamp - required"
              },
              "updatedAt": {
                "bsonType": "date",
                "description": "Update timestamp"
              }
            }
          }
        }
      """
    )

    database.runCommand(
      Document.parse(s"""
        {
          "collMod": "orders",
          "validator": ${validationRule.toJson}
        }
      """)
    )

    println("Orders collection created with indexes and validation")
  }

  @RollbackExecution
  def rollback(database: MongoDatabase): Unit = {
    database.getCollection("orders").drop()
    println("Orders collection dropped")
  }
}
