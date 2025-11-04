package com.mikelalvarezgo.socikutxa.shared.infrastructure.http

import cats.effect.IO
import cats.implicits._
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import io.circe.Encoder
import io.circe.syntax._
import org.http4s.Response
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.io._

object HttpErrorHandler {

  implicit class ValidationOps[A: Encoder](validation: Validation[IO[A]]) {
    def toHttpResponse: IO[Response[IO]] =
      validation.fold(
        errors => BadRequest(Map("errors" -> errors.toList.map(_.message)).asJson),
        ioResult =>
          ioResult
            .flatMap {
              case list: List[_] if list.isEmpty => NotFound("Resource not found")
              case result                        => Ok(result.asJson)
            }
            .handleErrorWith(error => InternalServerError(s"Internal server error: ${error.getMessage}"))
      )
  }
}
