package com.mikelalvarezgo.socikutxa.auth.infrastructure

import cats.effect.Sync
import org.mindrot.jbcrypt.BCrypt

class PasswordHashingService[F[_] : Sync] {

  def hashPassword(plainPassword: String): F[String] = Sync[F].delay {
    BCrypt.hashpw(plainPassword, BCrypt.gensalt())
  }

  def verifyPassword(plainPassword: String, hashedPassword: String): F[Boolean] = Sync[F].delay {
    BCrypt.checkpw(plainPassword, hashedPassword)
  }
}

object PasswordHashingService {
  def apply[F[_] : Sync](): PasswordHashingService[F] = new PasswordHashingService[F]()
}
