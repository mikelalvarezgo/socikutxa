package com.mikelalvarezgo.socikutxa.product.infrastructure

import cats.effect.IO
import com.mikelalvarezgo.socikutxa.product.application.GetProductsUseCase.GetProductsQuery
import com.mikelalvarezgo.socikutxa.product.application.ImportProductsUseCase.ImportProductsCommand
import com.mikelalvarezgo.socikutxa.product.application.{GetProductsUseCase, ImportProductsUseCase}
import com.mikelalvarezgo.socikutxa.product.domain.Product
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
        multipart =>
          multipart.parts.find(_.filename.exists(_.endsWith(".xlsx"))) match {
            case Some(filePart) =>
              filePart.body.compile.toList.flatMap {
                chunks =>
                  val bytes       = chunks.toArray
                  val inputStream = new java.io.ByteArrayInputStream(bytes)
                  XlsxProductParser.parse(inputStream) match {
                    case Right(products) =>
                      val command = ImportProductsCommand(products)
                      importProductsUseCase.execute(command).toHttpResponse
                    case Left(error)     =>
                      BadRequest(error.getMessage)
                  }
              }
            case None           =>
              BadRequest("No .xlsx file found in upload")
          }
      }
    case GET -> Root / "product"                   =>
      getProductsUseCase.execute(GetProductsQuery()).toHttpResponse
  }
}
