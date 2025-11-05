package com.mikelalvarezgo.socikutxa.user.domain.error

import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationError

case class InvalidUserId(value: String) extends ValidationError {
  val message: String = s"Invalid user id: $value"
}
