package com.mikelalvarezgo.socikutxa.order.domain

import com.mikelalvarezgo.socikutxa.user.domain.UserId
import com.mikelalvarezgo.socikutxa.product.domain.ProductId

import java.time.Instant

case class Order(
    id: OrderId,
    buyerId: UserId,
    sellerId: UserId,
    products: List[OrderProduct],
    status: OrderStatus,
    createdAt: Instant,
    updatedAt: Option[Instant]
) {
  def totalAmount: Double = products.map(p => p.priceAtOrder * p.quantity).sum

  def addProduct(product: OrderProduct): Order = this.copy(
    products = products :+ product,
    updatedAt = Some(Instant.now())
  )

  def removeProduct(productId: ProductId): Order = this.copy(
    products = products.filterNot(_.productId == productId),
    updatedAt = Some(Instant.now())
  )

  def updateProductQuantity(productId: ProductId, newQuantity: Int): Order = {
    val updatedProducts = products.map { p =>
      if (p.productId == productId) p.copy(quantity = newQuantity)
      else p
    }
    this.copy(products = updatedProducts, updatedAt = Some(Instant.now()))
  }
}

object Order {
  def create(
      buyerId: UserId,
      sellerId: UserId,
      products: List[OrderProduct]
  ): Order = Order(
    id = OrderId.create,
    buyerId = buyerId,
    sellerId = sellerId,
    products = products,
    status = OrderStatus.Draft,
    createdAt = Instant.now(),
    updatedAt = None
  )
}
