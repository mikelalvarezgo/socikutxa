package com.mikelalvarezgo.socikutxa.product.domain

case class Product(
    id: ProductId,
    productName: String,
    productCategory: ProductCategory,
    price: Double
)
