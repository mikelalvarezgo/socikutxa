package com.mikelalvarezgo.socikutxa.user.domain

import cats.data.Validated
import com.mikelalvarezgo.socikutxa.user.domain.error.InvalidEmail
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationErrorException

case class Email(value: String)

object Email {
  private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".r

  def unsafe(value: String): Email =
    ValidationErrorException.getOrThrow(fromString(value))

  def fromString(value: String): Validation[Email] =
    Validated.condNel(
        isValid(value),
        Email(value),
        InvalidEmail(value)
    )

  private def isValid(value: String): Boolean =
    emailRegex.matches(value)
}
