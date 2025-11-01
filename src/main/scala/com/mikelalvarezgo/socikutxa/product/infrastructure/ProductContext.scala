package com.mikelalvarezgo.socikutxa.product.infrastructure

import cats.effect.IO
import com.mikelalvarezgo.socikutxa.product.application.ImportProductsUseCase
import com.mikelalvarezgo.socikutxa.shared.infrastructure.dependency_injection.Context
import doobie.Transactor
import org.http4s.HttpRoutes

class ProductContext(transactor: Transactor[IO]) extends Context[IO] {

  private val productRepository     = new PostgresProductRepository(transactor)
  private val importProductsUseCase = new ImportProductsUseCase[IO](productRepository)
  private val controller            = new ProductController(importProductsUseCase)

  override val routes: HttpRoutes[IO] = controller.routes
}
