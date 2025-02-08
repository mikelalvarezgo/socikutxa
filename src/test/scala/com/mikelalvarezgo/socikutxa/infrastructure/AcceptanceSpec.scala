package com.mikelalvarezgo.socikutxa.infrastructure

import cats.data.OptionT
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.ValidationFutureT
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.{Assertion, BeforeAndAfterEach, OptionValues}

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.Try

abstract class AcceptanceSpec[CurrentContext <: TestContext]
    extends AnyWordSpec
    with Matchers
    with ScalaFutures
    with OptionValues
    with Eventually
    with BeforeAndAfterEach {

  implicit class ValidationTestSyntax[T](validation: ValidationFutureT[T]) {
    def getResult: T          = validation.toOption.get.futureValue
    def getFailure: Throwable = validation.toOption.get.failed.futureValue
  }

  implicit class OptionTTestSyntax[T](optionT: OptionT[Future, T]) {
    def getResult: T = optionT.value.futureValue.get
  }

  implicit override val patienceConfig: PatienceConfig = PatienceConfig(
    timeout = scaled(60.seconds),
    interval = scaled(500.millis)
  )
  type Test = CurrentContext => Assertion

  def context(): CurrentContext

  def runWithContext(testToExecute: Test): Assertion = {
    val testContext = context()
    val result      = Try(testToExecute(testContext))
    testContext.closeResources()
    result.get
  }
}
