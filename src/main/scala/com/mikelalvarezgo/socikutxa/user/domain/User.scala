package com.mikelalvarezgo.socikutxa.user.domain

import java.time.{Instant, LocalDate}

case class User(
    id: UserId,
    name: String,
    surname: String,
    email: String,
    password: String,
    birthdate: Option[LocalDate],
    phoneNumber: Option[String],
    createdAt: Instant,
    updatedAt: Instant
)
