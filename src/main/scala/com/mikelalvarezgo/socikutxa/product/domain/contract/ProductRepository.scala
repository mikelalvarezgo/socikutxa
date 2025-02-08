package com.mikelalvarezgo.socikutxa.product.domain.contract

import cats.data.OptionT
import com.mikelalvarezgo.socikutxa.product.domain.{Product, ProductId}

trait ProductRepository[P[_]] {

  def create(p: Product): P[Unit]

  def findBy(id: ProductId): OptionT[P, Product]

  def findAll(): P[List[Product]]

}
