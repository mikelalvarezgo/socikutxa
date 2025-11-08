package com.mikelalvarezgo.socikutxa.user.domain

import cats.data.Validated
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationError

sealed trait UserType {
  def value: String = getClass.getSimpleName.stripSuffix("$").toUpperCase
}

object UserType {
  case object Member   extends UserType
  case object Consumer extends UserType

  def parse(str: String): Option[UserType] = str.toUpperCase match {
    case "MEMBER"   => Some(Member)
    case "CONSUMER" => Some(Consumer)
    case _          => None
  }

  def fromString(str: String): Validation[UserType] =
    parse(str) match {
      case Some(userType) => Validated.validNel(userType)
      case None           => Validated.invalidNel(InvalidUserType(str))
    }
}

case class InvalidUserType(value: String) extends ValidationError {
  override def message: String = s"Invalid user type: $value"
}
