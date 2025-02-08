package com.mikelalvarezgo.socikutxa.product.infrastructure

import com.mikelalvarezgo.socikutxa.product.domain.{Product, ProductCategory, ProductId}
import doobie.Read

trait PostgresProductOps {

  implicit val productRead: Read[Product] = Read[(String, String, String, Double)].map {
    case (id, name, category, price) =>
      Product(ProductId.unsafe(id), name, ProductCategory.unsafe(category), price)
  }

}
