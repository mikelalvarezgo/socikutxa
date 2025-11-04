package com.mikelalvarezgo.socikutxa.modules.product.behaviour

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import cats.effect.unsafe.implicits.global
import com.mikelalvarezgo.socikutxa.infrastructure.BehaviourTestCase
import com.mikelalvarezgo.socikutxa.product.application.GetProductsUseCase
import com.mikelalvarezgo.socikutxa.product.application.GetProductsUseCase.GetProductsQuery
import com.mikelalvarezgo.socikutxa.product.domain.{Product, ProductCategory, ProductId}

class GetProductsUseCaseTest extends BehaviourTestCase {

  val underTest = new GetProductsUseCase(productRepository)

  "GetProductsUseCase" should {
    "return all products from repository" in {
      val expectedProducts = List(
        Product(
          ProductId.create,
          "Product 1",
          ProductCategory.Snack,
          10.5
        ),
        Product(
          ProductId.create,
          "Product 2",
          ProductCategory.Drink,
          5.0
        )
      )

      when(productRepository.findAll()).thenReturn(expectedProducts.pure[IO])

      val query  = GetProductsQuery()
      val result = underTest.execute(query)

      result.isValid shouldBe true
      result.toOption.get.unsafeRunSync() shouldBe expectedProducts
    }
  }
}
