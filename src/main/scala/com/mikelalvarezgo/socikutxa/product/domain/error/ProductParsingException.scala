package com.mikelalvarezgo.socikutxa.product.domain.error

import com.mikelalvarezgo.socikutxa.shared.infrastructure.error.InfrastructureError

final case class ProductParsingException(errorMessage: String) extends InfrastructureError {
  override def message: String = s"Error parsing products: $errorMessage"
}
