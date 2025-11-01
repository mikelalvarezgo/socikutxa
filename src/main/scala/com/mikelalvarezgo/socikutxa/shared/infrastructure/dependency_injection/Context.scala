package com.mikelalvarezgo.socikutxa.shared.infrastructure.dependency_injection
import org.http4s._
trait Context[P[_]] {

  val routes: HttpRoutes[P]
}
