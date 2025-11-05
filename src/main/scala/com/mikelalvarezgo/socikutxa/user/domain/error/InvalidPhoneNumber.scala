package com.mikelalvarezgo.socikutxa.user.domain.error

import com.mikelalvarezgo.socikutxa.shared.domain.error.ValidationError

case class InvalidPhoneNumber(value: String) extends ValidationError {
  val message: String = s"Invalid phone number: $value"
}
