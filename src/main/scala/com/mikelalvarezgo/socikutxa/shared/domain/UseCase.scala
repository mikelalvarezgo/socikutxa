package com.mikelalvarezgo.socikutxa.shared.domain

import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation

trait Request {
  type Response
}

trait Query extends Request

trait Command extends Request {
  override type Response = Unit
}
trait UseCase[T[_], R <: Request] {
  def execute(r: R): Validation[T[R#Response]]
}
