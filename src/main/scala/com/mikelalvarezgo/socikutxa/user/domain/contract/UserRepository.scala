package com.mikelalvarezgo.socikutxa.user.domain.contract

import cats.data.OptionT
import com.mikelalvarezgo.socikutxa.user.domain.{Email, User, UserId}

trait UserRepository[P[_]] {

  def save(user: User): P[Unit]

  def findBy(id: UserId): OptionT[P, User]

  def findByEmail(email: Email): OptionT[P, User]

}
