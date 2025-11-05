package com.mikelalvarezgo.socikutxa.modules.user.behaviour

import cats.effect.IO
import com.mikelalvarezgo.socikutxa.infrastructure.BehaviourTestCase
import com.mikelalvarezgo.socikutxa.user.application.CreateUserUseCase
import com.mikelalvarezgo.socikutxa.user.application.CreateUserUseCase.CreateUserCommand
import com.mikelalvarezgo.socikutxa.user.domain.error.{InvalidEmail, InvalidPhoneNumber}

import java.time.LocalDate

class CreateUserUseCaseTest extends BehaviourTestCase {

  val underTest = new CreateUserUseCase(userRepository)

  "CreateUserUseCase" should {
    "create user with valid data" in {
      when(userRepository.save(any)).thenReturn(IO.unit)

      val command = CreateUserCommand(
          name = "John",
          surname = "Doe",
          email = "john.doe@example.com",
          password = "securePassword123",
          birthdate = Some(LocalDate.of(1990, 1, 1)),
          phoneNumber = Some("+34600000000")
      )

      val result = underTest.execute(command)

      result.isValid shouldBe true
      verify(userRepository).save(any)
    }

    "return validation error for invalid email" in {
      val command = CreateUserCommand(
          name = "John",
          surname = "Doe",
          email = "invalid-email",
          password = "securePassword123",
          birthdate = Some(LocalDate.of(1990, 1, 1)),
          phoneNumber = Some("+34600000000")
      )

      val result = underTest.execute(command)

      result.isInvalid shouldBe true
      result.swap.toOption.get.toList should contain(InvalidEmail("invalid-email"))
    }

    "return validation error for invalid phone number" in {
      val command = CreateUserCommand(
          name = "John",
          surname = "Doe",
          email = "john.doe@example.com",
          password = "securePassword123",
          birthdate = Some(LocalDate.of(1990, 1, 1)),
          phoneNumber = Some("invalid-phone")
      )

      val result = underTest.execute(command)

      result.isInvalid shouldBe true
      result.swap.toOption.get.toList should contain(InvalidPhoneNumber("invalid-phone"))
    }

    "return multiple validation errors for invalid email and phone" in {
      val command = CreateUserCommand(
          name = "John",
          surname = "Doe",
          email = "invalid-email",
          password = "securePassword123",
          birthdate = Some(LocalDate.of(1990, 1, 1)),
          phoneNumber = Some("invalid-phone")
      )

      val result = underTest.execute(command)

      result.isInvalid shouldBe true
      val errors = result.swap.toOption.get.toList
      errors should contain(InvalidEmail("invalid-email"))
      errors should contain(InvalidPhoneNumber("invalid-phone"))
    }
  }
}
