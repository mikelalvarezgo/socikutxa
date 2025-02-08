package com.mikelalvarezgo.socikutxa.product.domain

import cats.data.Validated
import com.mikelalvarezgo.socikutxa.product.domain.error.InvalidProductId
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationErrorException
import com.mikelalvarezgo.socikutxa.shared.domain.model.Id

import java.util.UUID

case class ProductId(value: UUID) extends Id(value)

object ProductId {
  def unsafe(value: String): ProductId =
    ValidationErrorException.getOrThrow(fromString(value))

  def create: ProductId = ProductId(UUID.randomUUID())

  def fromString(value: String): Validation[ProductId] =
    Validated.condNel(
        Id.isValid(value),
        ProductId(UUID.fromString(value)),
        InvalidProductId(value)
    )
}
