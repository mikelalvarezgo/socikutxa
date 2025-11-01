package com.mikelalvarezgo.socikutxa.infrastructure

import cats.effect.IO
import com.mikelalvarezgo.socikutxa.shared.infrastructure.dependency_injection.Context

trait TestContext extends Context[IO] {
  def closeResources(): Unit
}
