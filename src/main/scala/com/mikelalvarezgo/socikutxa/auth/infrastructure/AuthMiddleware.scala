package com.mikelalvarezgo.socikutxa.auth.infrastructure

import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import org.http4s.Request
import org.http4s.server.{AuthMiddleware => Http4sAuthMiddleware}
import org.typelevel.ci._

object AuthMiddleware {

  private val BearerPrefix        = "Bearer "
  private val AuthorizationHeader = ci"Authorization"

  case class AuthenticatedUser(userId: String)

  def apply(jwtService: JwtService[IO]): Http4sAuthMiddleware[IO, AuthenticatedUser] = {
    type OptionTIO[A] = OptionT[IO, A]

    val authUser: Kleisli[OptionTIO, Request[IO], AuthenticatedUser] =
      Kleisli {
        request =>
          OptionT {
            extractToken(request) match {
              case Some(token) =>
                jwtService.validateToken(token).map {
                  case Right(userId) => Some(AuthenticatedUser(userId))
                  case Left(_)       => None
                }
              case None        => IO.pure(None)
            }
          }
      }

    Http4sAuthMiddleware(authUser)
  }

  private def extractToken(request: Request[IO]): Option[String] =
    request.headers
      .get(AuthorizationHeader)
      .map(_.head.value.replace(BearerPrefix, ""))
}
