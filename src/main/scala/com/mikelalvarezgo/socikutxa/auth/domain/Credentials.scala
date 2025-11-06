package com.mikelalvarezgo.socikutxa.auth.domain

import com.mikelalvarezgo.socikutxa.user.domain.Email

case class Credentials(
    email: Email,
    password: String
)
