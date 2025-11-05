package com.mikelalvarezgo.socikutxa.user.domain

import cats.data.Validated
import com.mikelalvarezgo.socikutxa.user.domain.error.InvalidPhoneNumber
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationErrorException

case class PhoneNumber(value: String)

object PhoneNumber {
  private val phoneRegex = "^\\+?[1-9]\\d{1,14}$".r

  def unsafe(value: String): PhoneNumber =
    ValidationErrorException.getOrThrow(fromString(value))

  def fromString(value: String): Validation[PhoneNumber] =
    Validated.condNel(
        isValid(value),
        PhoneNumber(value),
        InvalidPhoneNumber(value)
    )

  private def isValid(value: String): Boolean =
    phoneRegex.matches(value)
}
