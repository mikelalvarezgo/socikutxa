package com.mikelalvarezgo.socikutxa.order.domain.error

import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationError

case class InvalidOrderId(value: String) extends ValidationError {
  val message: String = s"Invalid order id: $value"
}
