package com.mikelalvarezgo.socikutxa.modules.auth.behaviour

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.mikelalvarezgo.socikutxa.auth.infrastructure.PasswordHashingService
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PasswordHashingServiceTest extends AnyWordSpec with Matchers {

  private val service = PasswordHashingService[IO]()

  "PasswordHashingService" should {
    "hash a plain password successfully" in {
      val plainPassword = "mySecurePassword123"

      val hashedPassword = service.hashPassword(plainPassword).unsafeRunSync()

      hashedPassword should not be empty
      hashedPassword should not be plainPassword
      hashedPassword should startWith("$2a$")
    }

    "verify a correct password against its hash" in {
      val plainPassword = "testPassword456"

      val hashedPassword = service.hashPassword(plainPassword).unsafeRunSync()
      val isValid        = service.verifyPassword(plainPassword, hashedPassword).unsafeRunSync()

      isValid shouldBe true
    }

    "reject an incorrect password against a hash" in {
      val plainPassword = "correctPassword"
      val wrongPassword = "wrongPassword"

      val hashedPassword = service.hashPassword(plainPassword).unsafeRunSync()
      val isValid        = service.verifyPassword(wrongPassword, hashedPassword).unsafeRunSync()

      isValid shouldBe false
    }

    "generate different hashes for the same password" in {
      val plainPassword = "samePassword"

      val hash1 = service.hashPassword(plainPassword).unsafeRunSync()
      val hash2 = service.hashPassword(plainPassword).unsafeRunSync()

      hash1 should not be hash2
      service.verifyPassword(plainPassword, hash1).unsafeRunSync() shouldBe true
      service.verifyPassword(plainPassword, hash2).unsafeRunSync() shouldBe true
    }

    "handle empty password" in {
      val emptyPassword = ""

      val hashedPassword = service.hashPassword(emptyPassword).unsafeRunSync()
      val isValid        = service.verifyPassword(emptyPassword, hashedPassword).unsafeRunSync()

      isValid shouldBe true
    }
  }
}
