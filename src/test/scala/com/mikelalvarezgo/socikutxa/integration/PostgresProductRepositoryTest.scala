package com.mikelalvarezgo.socikutxa.integration

import com.mikelalvarezgo.socikutxa.infrastructure.IntegrationTestCase
import com.mikelalvarezgo.socikutxa.infrastructure.stub.ProductStub
import com.mikelalvarezgo.socikutxa.product.infrastructure.PostgresProductRepository
import cats.effect.unsafe.implicits.global

class PostgresProductRepositoryTest extends IntegrationTestCase{

  val underTest = new PostgresProductRepository(postgresConfig.transactor)


  "PostgresProductRepository" should {
    "store products in database" in {
      val product = ProductStub.random()
      underTest.create(product)
      val fetchedProduct = underTest.findBy(product.id)
      fetchedProduct.isDefined.map { result =>
        result shouldBe true
      }.unsafeToFuture()
    }

    "fetch all products in database" in {
      val product1 = ProductStub.random()
      val product2 = ProductStub.random()
      underTest.create(product1)
      underTest.create(product2)
      val fetchedProduct = underTest.findAll()
      fetchedProduct.map { result =>
        result.size shouldBe 2
      }.unsafeToFuture()
    }
  }
}
