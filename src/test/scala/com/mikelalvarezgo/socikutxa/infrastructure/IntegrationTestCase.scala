package com.mikelalvarezgo.socikutxa.infrastructure

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import akka.actor.ActorSystem
import com.mikelalvarezgo.socikutxa.shared.domain.contract.Logger
import com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.postgres.PostgresConfig
import org.scalatest.{EitherValues, OptionValues}
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import com.typesafe.config.ConfigFactory

abstract class IntegrationTestCase
    extends AnyWordSpecLike
    with Matchers
    with OptionValues
    with ScalaFutures
    with Eventually
    with EitherValues {
  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = 60.seconds, interval = 500.millis)
  val config = ConfigFactory.defaultApplication()

  val postgresConfig = PostgresConfig.fromConfig(config.getConfig("app.postgres"))
  implicit val system: ActorSystem          = ActorSystem()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val logger: Logger               = DummyLogger
}
