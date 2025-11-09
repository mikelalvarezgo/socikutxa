package com.mikelalvarezgo.socikutxa.infrastructure

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import akka.actor.ActorSystem
import com.mikelalvarezgo.socikutxa.shared.domain.contract.Logger
import com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.postgres.PostgresConfig
import com.mikelalvarezgo.socikutxa.shared.infrastructure.persistence.mongo.{MongoConfig, MongockRunner}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, EitherValues, OptionValues}
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
    with EitherValues
    with BeforeAndAfterEach
    with BeforeAndAfterAll {
  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = 60.seconds, interval = 500.millis)
  val config                                           = ConfigFactory.defaultApplication()

  val postgresConfig                        = PostgresConfig.fromConfig(config.getConfig("app.postgres"))
  val mongoConfig                           = MongoConfig.fromConfig(config.getConfig("mongo"))
  implicit val system: ActorSystem          = ActorSystem()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val logger: Logger               = DummyLogger

  override def beforeAll(): Unit = {
    super.beforeAll()
    // Run MongoDB migrations before all tests
    new MongockRunner(mongoConfig).runMigrations()
  }
}
