package com.mikelalvarezgo.socikutxa.user.domain

import java.time.{Instant, LocalDate}

case class User(
    id: UserId,
    name: String,
    surname: String,
    email: Email,
    password: String,
    userType: UserType,
    birthdate: Option[LocalDate],
    phoneNumber: Option[PhoneNumber],
    createdAt: Instant,
    updatedAt: Instant
)
