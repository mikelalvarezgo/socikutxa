package com.mikelalvarezgo.socikutxa.product.domain.error

import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationError

case class InvalidProductId(value: String) extends ValidationError {
  override def message: String = s"Invalid productId $value"
}
