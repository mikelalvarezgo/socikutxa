package com.mikelalvarezgo.socikutxa.infrastructure

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import com.mikelalvarezgo.socikutxa.product.domain.contract.ProductRepository
import com.mikelalvarezgo.socikutxa.product.domain.Product
import com.mikelalvarezgo.socikutxa.user.domain.contract.UserRepository
import org.mockito.{ArgumentMatchersSugar, MockitoSugar}
import org.scalatest.{BeforeAndAfterAll, EitherValues, GivenWhenThen, OptionValues}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Millis, Span}
import org.scalatest.wordspec.AnyWordSpec
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.ValidationFutureT

abstract class BehaviourTestCase
    extends AnyWordSpec
    with OptionValues
    with ScalaFutures
    with GivenWhenThen
    with EitherValues
    with BeforeAndAfterAll
    with Matchers
    with MockitoSugar
    with ArgumentMatchersSugar {

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(
      timeout = scaled(Span(2000, Millis)),
      interval = scaled(Span(100, Millis))
  )

  implicit class ValidationTestSyntax[T](validation: ValidationFutureT[T]) {
    def getResult: T          = validation.toOption.get.futureValue
    def getFailure: Throwable = validation.toOption.get.failed.futureValue
  }

  // Repositories
  val productRepository = mock[ProductRepository[IO]]
  val userRepository    = mock[UserRepository[IO]]
}
