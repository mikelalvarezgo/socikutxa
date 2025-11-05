package com.mikelalvarezgo.socikutxa.infrastructure.stub

import com.mikelalvarezgo.socikutxa.user.domain.{Email, PhoneNumber, User, UserId}

import java.time.{Instant, LocalDate}
import java.util.UUID
import scala.util.Random

object UserStub {

  def random(): User = User(
      UserId.apply(UUID.randomUUID()),
      Random.nextString(10),
      Random.nextString(10),
      Email.unsafe("${Random.nextString(5)}@test.com"),
      Random.nextString(60),
      Some(LocalDate.now().minusYears(Random.nextInt(50))),
      Some(PhoneNumber.unsafe("+34${Random.nextInt(999999999)}")),
      Instant.now(),
      Instant.now()
  )
}
