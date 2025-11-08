package com.mikelalvarezgo.socikutxa.modules.user.integration

import com.mikelalvarezgo.socikutxa.infrastructure.IntegrationTestCase
import com.mikelalvarezgo.socikutxa.infrastructure.stub.UserStub
import com.mikelalvarezgo.socikutxa.user.domain.Email
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

    "find user by email" in {
      val user          = UserStub.random()
      val email         = Email.unsafe("findbyemail@test.com")
      val userWithEmail = user.copy(email = email)

      underTest.save(userWithEmail).unsafeToFuture()

      val fetchedUser = underTest.findByEmail(email)
      fetchedUser.value
        .map {
          case Some(foundUser) =>
            foundUser.email shouldBe email
            foundUser.id shouldBe userWithEmail.id
          case None            =>
            fail("User should be found by email")
        }
        .unsafeToFuture()
    }
  }
}
