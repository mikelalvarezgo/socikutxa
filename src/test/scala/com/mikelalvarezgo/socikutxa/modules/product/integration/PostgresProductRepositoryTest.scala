package com.mikelalvarezgo.socikutxa.modules.product.integration

import com.mikelalvarezgo.socikutxa.infrastructure.IntegrationTestCase
import com.mikelalvarezgo.socikutxa.infrastructure.stub.ProductStub
import com.mikelalvarezgo.socikutxa.product.infrastructure.PostgresProductRepository
import cats.effect.unsafe.implicits.global
import org.scalatest.BeforeAndAfterEach

class PostgresProductRepositoryTest extends IntegrationTestCase {

  val underTest = new PostgresProductRepository(postgresConfig.transactor)

  override def beforeEach(): Unit = {
    underTest.deleteAll().unsafeRunSync()
  }

  "PostgresProductRepository" should {
    "store products in database" in {
      val product = ProductStub.random()
      underTest.create(product).unsafeRunSync()

      val fetchedProduct = underTest.findBy(product.id)
      fetchedProduct.isDefined
        .map {
          result =>
            result shouldBe true
        }
        .unsafeRunSync()
    }

    "fetch all products in database" in {
      val product1 = ProductStub.random()
      val product2 = ProductStub.random()
      underTest.create(product1).unsafeRunSync()
      underTest.create(product2).unsafeRunSync()

      val fetchedProduct = underTest.findAll()
      fetchedProduct
        .map {
          result =>
            result.size shouldBe 2
        }
        .unsafeRunSync()
    }
  }
}
