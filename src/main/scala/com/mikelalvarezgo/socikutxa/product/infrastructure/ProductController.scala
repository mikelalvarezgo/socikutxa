package com.mikelalvarezgo.socikutxa.product.infrastructure

import cats.effect.IO
import com.mikelalvarezgo.socikutxa.product.application.GetProductsUseCase.GetProductsQuery
import com.mikelalvarezgo.socikutxa.product.application.{GetProductsUseCase, ImportProductsUseCase}
import com.mikelalvarezgo.socikutxa.product.domain.Product
import com.mikelalvarezgo.socikutxa.product.infrastructure.ProductJsonCodec._
import com.mikelalvarezgo.socikutxa.shared.infrastructure.http.HttpErrorHandler._
import io.circe.Encoder
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.Method.POST
import org.http4s.dsl.io._
import org.http4s.multipart.Multipart

class ProductController(
    importProductsUseCase: ImportProductsUseCase[IO],
    getProductsUseCase: GetProductsUseCase[IO]
) {
  implicit val productListEncoder: Encoder[List[Product]] = Encoder.encodeList[Product]

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "product" / "upload" =>
      req.decode[Multipart[IO]] {
        _ =>
          Ok("Upload received")
      }
    case GET -> Root / "product"                   =>
      getProductsUseCase.execute(GetProductsQuery()).toHttpResponse
  }
}
