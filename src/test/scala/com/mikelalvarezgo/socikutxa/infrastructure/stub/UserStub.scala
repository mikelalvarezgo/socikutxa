package com.mikelalvarezgo.socikutxa.infrastructure.stub

import com.mikelalvarezgo.socikutxa.user.domain.{Email, PhoneNumber, User, UserId, UserType}

import java.time.{Instant, LocalDate}
import java.util.UUID
import scala.util.Random

object UserStub {

  def random(): User = User(
      UserId.apply(UUID.randomUUID()),
      Random.nextString(10),
      Random.nextString(10),
      Email.unsafe("abcdc@test.com"),
      Random.nextString(60),
      UserType.Consumer,
      Some(LocalDate.now().minusYears(Random.nextInt(50))),
      Some(PhoneNumber.unsafe(s"+34${Random.nextInt(999999999)}")),
      Instant.now(),
      Instant.now()
  )
}
