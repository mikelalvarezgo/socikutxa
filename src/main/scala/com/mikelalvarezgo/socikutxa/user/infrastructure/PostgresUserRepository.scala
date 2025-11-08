package com.mikelalvarezgo.socikutxa.user.infrastructure

import cats.data.OptionT
import cats.effect.IO
import com.mikelalvarezgo.socikutxa.user.domain.{Email, User, UserId, UserType}
import com.mikelalvarezgo.socikutxa.user.domain.contract.UserRepository
import doobie.Transactor
import doobie.implicits.toSqlInterpolator
import doobie.implicits._
import doobie.implicits.javatimedrivernative._

class PostgresUserRepository(transactor: Transactor[IO])
    extends UserRepository[IO]
    with PostgresUserOps {
  override def save(user: User): IO[Unit] = {
    val userTypeStr = user.userType.value
    sql"""
          INSERT INTO "user" (id, name, surname, email, password, user_type, birthdate, phone_number, created_at, updated_at)
          VALUES (${user.id.raw}, ${user.name}, ${user.surname}, ${user.email.value}, ${user.password}, $userTypeStr::user_type, ${user.birthdate}, ${user.phoneNumber
        .map(_.value)}, ${user.createdAt}, ${user.updatedAt})
        """.update.run.transact(transactor).void
  }

  override def findBy(id: UserId): OptionT[IO, User] = {
    OptionT[IO, User](
        sql"""
        SELECT id, name, surname, email, password, user_type, birthdate, phone_number, created_at, updated_at FROM "user" WHERE id = ${id.raw}
      """.query[User]
          .option
          .transact(transactor)
    )
  }

  override def findByEmail(email: Email): OptionT[IO, User] = {
    OptionT[IO, User](
        sql"""
        SELECT id, name, surname, email, password, user_type, birthdate, phone_number, created_at, updated_at FROM "user" WHERE email = ${email.value}
      """.query[User]
          .option
          .transact(transactor)
    )
  }
}
