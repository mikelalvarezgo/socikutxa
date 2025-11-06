package com.mikelalvarezgo.socikutxa.auth.application

import cats.Monad
import cats.implicits._
import com.mikelalvarezgo.socikutxa.auth.domain.{AuthToken, Credentials}
import com.mikelalvarezgo.socikutxa.auth.infrastructure.{JwtService, PasswordHashingService}
import com.mikelalvarezgo.socikutxa.user.domain.contract.UserRepository

class AuthenticateUserUseCase[P[_] : Monad](
    userRepository: UserRepository[P],
    passwordHashingService: PasswordHashingService[P],
    jwtService: JwtService[P]
) {

  def authenticate(credentials: Credentials): P[Either[AuthenticationError, AuthToken]] = {
    userRepository
      .findByEmail(credentials.email)
      .value
      .flatMap {
        case None       =>
          Monad[P].pure(Left(InvalidCredentials(AuthenticateUserUseCase.UserNotFoundError)))
        case Some(user) =>
          passwordHashingService
            .verifyPassword(credentials.password, user.password)
            .flatMap {
              isValid =>
                if (isValid) {
                  jwtService
                    .generateToken(user.id.raw)
                    .map(
                        token => Right(token)
                    )
                }
                else {
                  Monad[P]
                    .pure(Left(InvalidCredentials(AuthenticateUserUseCase.InvalidPasswordError)))
                }
            }
      }
  }
}

object AuthenticateUserUseCase {
  private val UserNotFoundError    = "User not found"
  private val InvalidPasswordError = "Invalid password"
}

sealed trait AuthenticationError {
  def message: String
}

case class InvalidCredentials(message: String) extends AuthenticationError
