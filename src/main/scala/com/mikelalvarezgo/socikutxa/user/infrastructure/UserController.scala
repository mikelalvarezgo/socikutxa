package com.mikelalvarezgo.socikutxa.user.infrastructure

import cats.effect.IO
import com.mikelalvarezgo.socikutxa.user.application.CreateUserUseCase
import com.mikelalvarezgo.socikutxa.user.application.CreateUserUseCase.CreateUserCommand
import com.mikelalvarezgo.socikutxa.shared.infrastructure.http.HttpErrorHandler._
import io.circe.generic.auto._
import org.http4s.HttpRoutes
import org.http4s.Method.POST
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.io._

import java.time.LocalDate

class UserController(
    createUserUseCase: CreateUserUseCase[IO]
) {

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "user" =>
      req.decode[CreateUserRequest] {
        request =>
          val command = CreateUserCommand(
              name = request.name,
              surname = request.surname,
              email = request.email,
              password = request.password,
              birthdate = request.birthdate,
              phoneNumber = request.phoneNumber
          )
          createUserUseCase.execute(command).toHttpResponse
      }
  }
}

case class CreateUserRequest(
    name: String,
    surname: String,
    email: String,
    password: String,
    birthdate: Option[LocalDate],
    phoneNumber: Option[String]
)
