package com.mikelalvarezgo.socikutxa.shared.domain.contract.monitoring

import scala.concurrent.{ExecutionContext, Future}

import cats.Monad
import cats.implicits._

trait Histogram {
  def record(value: Long): Unit

  def record[T](codeToBenchmark: => T): T = {
    val before = System.nanoTime()
    val result = codeToBenchmark
    val after  = System.nanoTime()

    record(after - before)

    result
  }

  def recordFuture[T](codeToBenchmark: => Future[T])(implicit ec: ExecutionContext): Future[T] = {
    val before = System.nanoTime()
    val result = codeToBenchmark

    result.foreach(
        _ => record(System.nanoTime() - before)
    )

    result
  }

  def recordFutureOnEitherRight[P[_] : Monad, T, U](
      codeToBenchmark: => P[Either[T, U]]
  ): P[Either[T, Unit]] = {
    val before = System.nanoTime()
    val result = codeToBenchmark

    result.map(
        eitherResult =>
          eitherResult.map(
              _ => record(System.nanoTime() - before)
          )
    )
  }
}
