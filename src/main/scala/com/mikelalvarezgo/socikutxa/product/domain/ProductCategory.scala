package com.mikelalvarezgo.socikutxa.product.domain

import ca.mrvisser.sealerate
import cats.data.Validated
import com.mikelalvarezgo.socikutxa.product.domain.error.InvalidProductCategory
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationErrorException

sealed trait ProductCategory {
  def value: String
}

object ProductCategory {
  case object Drink extends ProductCategory {
    override def value: String = "Drink"
  }

  case object AlcoholicDrink extends ProductCategory {
    override def value: String = "AlcoholicDrink"
  }

  case object Fee extends ProductCategory {
    override def value: String = "Fee"
  }

  case object Snack extends ProductCategory {
    override def value: String = "Snack"
  }

  def values: Set[ProductCategory] = sealerate.values[ProductCategory]

  def unsafe(value: String): ProductCategory =
    ValidationErrorException.getOrThrow(fromString(value))

  def fromString(value: String): Validation[ProductCategory]     =
    Validated.condNel(
        parseValue(value).isDefined,
        parseValue(value).get,
        InvalidProductCategory(value)
    )
  private def parseValue(value: String): Option[ProductCategory] = values.find(_.value == value)
}
