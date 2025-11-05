package com.mikelalvarezgo.socikutxa.user.infrastructure

import com.mikelalvarezgo.socikutxa.user.domain.{Email, PhoneNumber, User, UserId}
import doobie.Read
import doobie.implicits.javatimedrivernative._

import java.time.{Instant, LocalDate}

trait PostgresUserOps {

  implicit val userRead: Read[User] = Read[
      (String, String, String, String, String, Option[LocalDate], Option[String], Instant, Instant)
  ].map {
    case (id, name, surname, email, password, birthdate, phoneNumber, createdAt, updatedAt) =>
      User(
          UserId.unsafe(id),
          name,
          surname,
          Email.unsafe(email),
          password,
          birthdate,
          phoneNumber.map(PhoneNumber.unsafe),
          createdAt,
          updatedAt
      )
  }

}
