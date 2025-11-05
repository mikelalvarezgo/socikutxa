package com.mikelalvarezgo.socikutxa.user.application

import cats.implicits._
import com.mikelalvarezgo.socikutxa.user.application.CreateUserUseCase.CreateUserCommand
import com.mikelalvarezgo.socikutxa.user.domain.contract.UserRepository
import com.mikelalvarezgo.socikutxa.user.domain.{Email, PhoneNumber, User, UserId}
import com.mikelalvarezgo.socikutxa.shared.domain.error.Validation.Validation
import com.mikelalvarezgo.socikutxa.shared.domain.{Command, UseCase}

import java.time.{Instant, LocalDate}

class CreateUserUseCase[P[_]](userRepository: UserRepository[P])
    extends UseCase[P, CreateUserCommand] {
  override def execute(r: CreateUserCommand): Validation[P[Unit]] = {
    validate(r).map {
      validatedUser =>
        userRepository.save(validatedUser)
    }
  }

  private def validate(r: CreateUserCommand): Validation[User] = {
    (
        UserId.create.validNel,
        r.name.validNel,
        r.surname.validNel,
        Email.fromString(r.email),
        r.password.validNel,
        r.birthdate.validNel,
        r.phoneNumber.traverse(PhoneNumber.fromString),
        Instant.now().validNel,
        Instant.now().validNel
    ).mapN(User.apply)
  }
}

object CreateUserUseCase {
  case class CreateUserCommand(
      name: String,
      surname: String,
      email: String,
      password: String,
      birthdate: Option[LocalDate],
      phoneNumber: Option[String]
  ) extends Command
}
