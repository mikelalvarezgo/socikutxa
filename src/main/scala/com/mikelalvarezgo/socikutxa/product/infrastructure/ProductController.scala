package com.mikelalvarezgo.socikutxa.product.infrastructure

import cats.effect.IO
import com.mikelalvarezgo.socikutxa.product.application.ImportProductsUseCase
import org.http4s.HttpRoutes
import org.http4s.Method.POST
import org.http4s.dsl.io._
import org.http4s.multipart.Multipart

class ProductController(
    importProductsUseCase: ImportProductsUseCase[IO]
) {
  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "product" / "upload" =>
      req.decode[Multipart[IO]] {
        _ =>
          Ok("Upload received")
      }
  }
}
