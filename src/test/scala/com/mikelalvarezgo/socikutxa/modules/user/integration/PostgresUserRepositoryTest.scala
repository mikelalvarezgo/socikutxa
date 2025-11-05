package com.mikelalvarezgo.socikutxa.modules.user.integration

import com.mikelalvarezgo.socikutxa.infrastructure.IntegrationTestCase
import com.mikelalvarezgo.socikutxa.infrastructure.stub.UserStub
import com.mikelalvarezgo.socikutxa.user.infrastructure.PostgresUserRepository
import cats.effect.unsafe.implicits.global

class PostgresUserRepositoryTest extends IntegrationTestCase {

  val underTest = new PostgresUserRepository(postgresConfig.transactor)

  "PostgresUserRepository" should {
    "store users in database" in {
      val user        = UserStub.random()
      underTest.save(user)
      val fetchedUser = underTest.findBy(user.id)
      fetchedUser.isDefined
        .map {
          result =>
            result shouldBe true
        }
        .unsafeToFuture()
    }
  }
}
