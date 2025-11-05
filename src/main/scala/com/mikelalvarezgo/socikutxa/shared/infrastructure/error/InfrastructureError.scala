package com.mikelalvarezgo.socikutxa.shared.infrastructure.error

trait InfrastructureError extends Throwable {
  def message: String

  override def getMessage: String = message
}
