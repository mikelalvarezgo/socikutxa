package com.mikelalvarezgo.socikutxa.product.domain.error

import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationError

case class InvalidProductCategory(value: String) extends ValidationError {
  override def message: String = s"Invalid product category $value"
}
