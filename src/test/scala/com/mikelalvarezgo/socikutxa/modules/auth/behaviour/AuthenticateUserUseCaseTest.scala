package com.mikelalvarezgo.socikutxa.modules.auth.behaviour

import cats.data.OptionT
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.mikelalvarezgo.socikutxa.auth.application.{AuthenticateUserUseCase, InvalidCredentials}
import com.mikelalvarezgo.socikutxa.auth.domain.Credentials
import com.mikelalvarezgo.socikutxa.auth.infrastructure.{JwtService, PasswordHashingService}
import com.mikelalvarezgo.socikutxa.infrastructure.stub.UserStub
import com.mikelalvarezgo.socikutxa.user.domain._
import com.mikelalvarezgo.socikutxa.user.domain.contract.UserRepository
import org.mockito.ArgumentMatchersSugar.any
import org.mockito.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class AuthenticateUserUseCaseTest extends AnyWordSpec with Matchers with MockitoSugar {

  private val mockUserRepository     = mock[UserRepository[IO]]
  private val passwordHashingService = PasswordHashingService[IO]()
  private val jwtService             = JwtService[IO]("test-secret", 3600)
  private val useCase                =
    new AuthenticateUserUseCase[IO](mockUserRepository, passwordHashingService, jwtService)

  "AuthenticateUserUseCase" should {
    "authenticate successfully with valid credentials" in {
      val email          = Email("test@example.com")
      val plainPassword  = "password123"
      val hashedPassword = passwordHashingService.hashPassword(plainPassword).unsafeRunSync()

      val user = UserStub
        .random()
        .copy(
            email = email,
            password = hashedPassword
        )

      when(mockUserRepository.findByEmail(email)).thenReturn(OptionT.some[IO](user))

      val credentials = Credentials(email, plainPassword)
      val result      = useCase.authenticate(credentials).unsafeRunSync()

      result.isRight shouldBe true
      result.map(_.userId shouldBe user.id.raw)
    }

    "fail authentication when user is not found" in {
      val email       = Email("notfound@example.com")
      val credentials = Credentials(email, "password")

      when(mockUserRepository.findByEmail(email)).thenReturn(OptionT.none[IO, User])

      val result = useCase.authenticate(credentials).unsafeRunSync()

      result shouldBe Left(InvalidCredentials("User not found"))
    }

    "fail authentication with invalid password" in {
      val email           = Email("test@example.com")
      val correctPassword = "correctPassword"
      val wrongPassword   = "wrongPassword"
      val hashedPassword  = passwordHashingService.hashPassword(correctPassword).unsafeRunSync()

      val user = UserStub
        .random()
        .copy(
            email = email,
            password = hashedPassword
        )

      when(mockUserRepository.findByEmail(email)).thenReturn(OptionT.some[IO](user))

      val credentials = Credentials(email, wrongPassword)
      val result      = useCase.authenticate(credentials).unsafeRunSync()

      result shouldBe Left(InvalidCredentials("Invalid password"))
    }

    "return a valid JWT token on successful authentication" in {
      val email          = Email("valid@example.com")
      val plainPassword  = "securePass"
      val hashedPassword = passwordHashingService.hashPassword(plainPassword).unsafeRunSync()

      val user = UserStub
        .random()
        .copy(
            email = email,
            password = hashedPassword
        )

      when(mockUserRepository.findByEmail(email)).thenReturn(OptionT.some[IO](user))

      val credentials = Credentials(email, plainPassword)
      val result      = useCase.authenticate(credentials).unsafeRunSync()

      result.isRight shouldBe true
      result.foreach {
        token =>
          token.token should not be empty
          token.userId shouldBe user.id.raw
          jwtService.validateToken(token.token).unsafeRunSync() shouldBe Right(user.id.raw)
      }
    }
  }
}
