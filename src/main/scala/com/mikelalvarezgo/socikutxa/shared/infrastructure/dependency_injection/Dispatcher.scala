package com.mikelalvarezgo.socikutxa.shared.infrastructure.dependency_injection

import com.mikelalvarezgo.socikutxa.shared.domain.Request
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.ValidationFutureT

trait Dispatcher {
  type ==>[A, B] = PartialFunction[A, B]

  def dispatch: Request ==> ValidationFutureT[Unit]
}
