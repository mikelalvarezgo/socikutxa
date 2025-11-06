package com.mikelalvarezgo.socikutxa.user.infrastructure

import cats.data.OptionT
import cats.effect.IO
import com.mikelalvarezgo.socikutxa.user.domain.{Email, User, UserId}
import com.mikelalvarezgo.socikutxa.user.domain.contract.UserRepository
import doobie.Transactor
import doobie.implicits.toSqlInterpolator
import doobie.implicits._
import doobie.implicits.javatimedrivernative._

class PostgresUserRepository(transactor: Transactor[IO])
    extends UserRepository[IO]
    with PostgresUserOps {
  override def save(user: User): IO[Unit] = {
    sql"""
          INSERT INTO "user" (id, name, surname, email, password, birthdate, phone_number, created_at, updated_at)
          VALUES (${user.id.raw}, ${user.name}, ${user.surname}, ${user.email.value}, ${user.password}, ${user.birthdate}, ${user.phoneNumber
        .map(_.value)}, ${user.createdAt}, ${user.updatedAt})
        """.update.run.transact(transactor).void
  }

  override def findBy(id: UserId): OptionT[IO, User] = {
    OptionT[IO, User](
        sql"""
        SELECT id, name, surname, email, password, birthdate, phone_number, created_at, updated_at FROM "user" WHERE id = ${id.raw}
      """.query[User]
          .option
          .transact(transactor)
    )
  }

  override def findByEmail(email: Email): OptionT[IO, User] = {
    OptionT[IO, User](
        sql"""
        SELECT id, name, surname, email, password, birthdate, phone_number, created_at, updated_at FROM "user" WHERE email = ${email.value}
      """.query[User]
          .option
          .transact(transactor)
    )
  }
}
