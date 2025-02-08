package com.mikelalvarezgo.socikutxa.infrastructure.stub

import com.mikelalvarezgo.socikutxa.product.domain.{Product, ProductCategory, ProductId}

import java.util.UUID
import scala.util.Random

object ProductStub {

  def random(): Product = Product(
    ProductId.apply(UUID.randomUUID()),
    Random.nextString(10),
    ProductCategory.values.head,
    Random.nextFloat().abs,
  )
}
