package com.mikelalvarezgo.socikutxa.infrastructure

import com.mikelalvarezgo.socikutxa.shared.infrastructure.dependency_injection.Context

trait TestContext extends Context {
  def closeResources(): Unit
}
