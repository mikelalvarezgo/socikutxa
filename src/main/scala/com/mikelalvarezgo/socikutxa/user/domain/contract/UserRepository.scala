package com.mikelalvarezgo.socikutxa.user.domain.contract

import cats.data.OptionT
import com.mikelalvarezgo.socikutxa.user.domain.{User, UserId}

trait UserRepository[P[_]] {

  def save(user: User): P[Unit]

  def findBy(id: UserId): OptionT[P, User]

}
