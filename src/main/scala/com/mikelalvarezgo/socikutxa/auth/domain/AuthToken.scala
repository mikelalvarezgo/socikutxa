package com.mikelalvarezgo.socikutxa.auth.domain

import java.time.Instant

case class AuthToken(
    token: String,
    userId: String,
    expiresAt: Instant
)
