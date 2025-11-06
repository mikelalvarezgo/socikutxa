package com.mikelalvarezgo.socikutxa.auth.infrastructure

import cats.effect.Sync
import com.mikelalvarezgo.socikutxa.auth.domain.AuthToken
import io.circe.syntax._
import io.circe.Json
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.{Clock, Instant}
import scala.util.{Failure, Success}

class JwtService[F[_] : Sync](
    secret: String,
    expirationSeconds: Long = JwtService.DefaultExpirationSeconds
) {

  private implicit val clock: Clock = Clock.systemUTC()

  def generateToken(userId: String): F[AuthToken] = Sync[F].delay {
    val now       = Instant.now()
    val expiresAt = now.plusSeconds(expirationSeconds)

    val claim = JwtClaim(
        content = Json.obj(JwtService.UserIdField -> userId.asJson).noSpaces,
        expiration = Some(expiresAt.getEpochSecond),
        issuedAt = Some(now.getEpochSecond)
    )

    val token = JwtCirce.encode(claim, secret, JwtAlgorithm.HS256)
    AuthToken(token, userId, expiresAt)
  }

  def validateToken(token: String): F[Either[String, String]] = Sync[F].delay {
    JwtCirce.decode(token, secret, Seq(JwtAlgorithm.HS256)) match {
      case Success(claim)     =>
        io.circe.parser
          .parse(claim.content)
          .flatMap(_.hcursor.get[String](JwtService.UserIdField)) match {
          case Right(userId) => Right(userId)
          case Left(error)   => Left(s"${JwtService.InvalidTokenContentError}: ${error.getMessage}")
        }
      case Failure(exception) => Left(s"${JwtService.InvalidTokenError}: ${exception.getMessage}")
    }
  }
}

object JwtService {
  private val UserIdField              = "userId"
  private val InvalidTokenContentError = "Invalid token content"
  private val InvalidTokenError        = "Invalid token"

  val DefaultExpirationSeconds: Long = 3600

  def apply[F[_] : Sync](
      secret: String,
      expirationSeconds: Long = DefaultExpirationSeconds
  ): JwtService[F] =
    new JwtService[F](secret, expirationSeconds)
}
