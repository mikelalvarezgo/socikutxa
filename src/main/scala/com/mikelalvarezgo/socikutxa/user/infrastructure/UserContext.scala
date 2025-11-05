package com.mikelalvarezgo.socikutxa.user.infrastructure

import cats.effect.IO
import com.mikelalvarezgo.socikutxa.user.application.CreateUserUseCase
import com.mikelalvarezgo.socikutxa.shared.infrastructure.dependency_injection.Context
import doobie.Transactor
import org.http4s.HttpRoutes

class UserContext(transactor: Transactor[IO]) extends Context[IO] {

  private val userRepository    = new PostgresUserRepository(transactor)
  private val createUserUseCase = new CreateUserUseCase[IO](userRepository)
  private val controller        = new UserController(createUserUseCase)

  override val routes: HttpRoutes[IO] = controller.routes
}
