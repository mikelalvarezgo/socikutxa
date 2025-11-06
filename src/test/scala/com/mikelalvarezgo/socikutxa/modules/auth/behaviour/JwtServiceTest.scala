package com.mikelalvarezgo.socikutxa.modules.auth.behaviour

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.mikelalvarezgo.socikutxa.auth.infrastructure.JwtService
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class JwtServiceTest extends AnyWordSpec with Matchers {

  private val secret     = "test-secret-key-for-jwt-token"
  private val jwtService = JwtService[IO](secret, 3600)

  "JwtService" should {
    "generate a valid token for a userId" in {
      val userId = "user-123"

      val token = jwtService.generateToken(userId).unsafeRunSync()

      token.userId shouldBe userId
      token.token should not be empty
      token.expiresAt.getEpochSecond should be > System.currentTimeMillis() / 1000
    }

    "validate a valid token and extract userId" in {
      val userId = "user-456"

      val token  = jwtService.generateToken(userId).unsafeRunSync()
      val result = jwtService.validateToken(token.token).unsafeRunSync()

      result shouldBe Right(userId)
    }

    "fail to validate an invalid token" in {
      val invalidToken = "invalid.token.here"

      val result = jwtService.validateToken(invalidToken).unsafeRunSync()

      result.isLeft shouldBe true
    }

    "fail to validate a token signed with different secret" in {
      val userId           = "user-789"
      val differentService = JwtService[IO]("different-secret", 3600)

      val token  = differentService.generateToken(userId).unsafeRunSync()
      val result = jwtService.validateToken(token.token).unsafeRunSync()

      result.isLeft shouldBe true
    }

    "generate tokens with correct expiration time" in {
      val userId           = "user-expiry"
      val customExpiration = 7200L

      val customService = JwtService[IO](secret, customExpiration)
      val token         = customService.generateToken(userId).unsafeRunSync()

      val expectedExpiry = System.currentTimeMillis() / 1000 + customExpiration
      token.expiresAt.getEpochSecond shouldBe expectedExpiry +- 2
    }
  }
}
