package com.mikelalvarezgo.socikutxa.user.domain.error

import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationError

case class InvalidEmail(value: String) extends ValidationError {
  val message: String = s"Invalid email: $value"
}
