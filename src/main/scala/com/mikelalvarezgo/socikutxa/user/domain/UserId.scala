package com.mikelalvarezgo.socikutxa.user.domain

import cats.data.Validated
import com.mikelalvarezgo.socikutxa.user.domain.error.InvalidUserId
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationErrorException
import com.mikelalvarezgo.socikutxa.shared.domain.model.Id

import java.util.UUID

case class UserId(value: UUID) extends Id(value)

object UserId {
  def unsafe(value: String): UserId =
    ValidationErrorException.getOrThrow(fromString(value))

  def create: UserId = UserId(UUID.randomUUID())

  def fromString(value: String): Validation[UserId] =
    Validated.condNel(
        Id.isValid(value),
        UserId(UUID.fromString(value)),
        InvalidUserId(value)
    )
}
