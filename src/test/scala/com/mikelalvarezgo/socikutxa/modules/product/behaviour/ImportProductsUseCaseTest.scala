package com.mikelalvarezgo.socikutxa.modules.product.behaviour

import cats.data.NonEmptyList
import cats.data.Validated.Invalid
import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import com.mikelalvarezgo.socikutxa.infrastructure.BehaviourTestCase
import com.mikelalvarezgo.socikutxa.product.application.ImportProductsUseCase
import com.mikelalvarezgo.socikutxa.product.application.ImportProductsUseCase.ProductParsed
import com.mikelalvarezgo.socikutxa.product.domain.ProductCategory
import com.mikelalvarezgo.socikutxa.product.domain.error.InvalidProductCategory

class ImportProductsUseCaseTest extends BehaviourTestCase {

  val underTest = new ImportProductsUseCase(productRepository)

  "PostgresProductRepository" should {
    "return validation error if category is not correct" in {
      val command = ImportProductsUseCase.ImportProductsCommand(
          products = List(
              ProductParsed(
                  "name",
                  1,
                  "wrongcateogry"
              )
          )
      )
      val result  = underTest.execute(command)
      result.isInvalid shouldBe true
      result shouldBe Invalid(NonEmptyList.one(InvalidProductCategory("wrongcateogry")))
    }
    "save all products in repository" in {
      val command = ImportProductsUseCase.ImportProductsCommand(
          products = List(
              ProductParsed(
                  "name",
                  1,
                  ProductCategory.Snack.value
              )
          )
      )
      when(productRepository.create(any)).thenReturn(().pure[IO])
      val result  = underTest.execute(command)
      result.isValid shouldBe true
    }
  }

}
