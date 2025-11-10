package com.mikelalvarezgo.socikutxa.order.domain

import com.mikelalvarezgo.socikutxa.product.domain.ProductId

case class OrderProduct(
    productId: ProductId,
    quantity: Int,
    priceAtOrder: Double
)
