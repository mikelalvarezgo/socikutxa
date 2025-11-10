package com.mikelalvarezgo.socikutxa.order.domain

import cats.data.Validated
import com.mikelalvarezgo.socikutxa.order.domain.error.InvalidOrderId
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationErrorException
import com.mikelalvarezgo.socikutxa.shared.domain.model.Id

import java.util.UUID

case class OrderId(value: UUID) extends Id(value)

object OrderId {
  def unsafe(value: String): OrderId =
    ValidationErrorException.getOrThrow(fromString(value))

  def create: OrderId = OrderId(UUID.randomUUID())

  def fromString(value: String): Validation[OrderId] =
    Validated.condNel(
        Id.isValid(value),
        OrderId(UUID.fromString(value)),
        InvalidOrderId(value)
    )
}
