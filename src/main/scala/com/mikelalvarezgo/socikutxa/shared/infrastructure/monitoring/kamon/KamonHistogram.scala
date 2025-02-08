package com.mikelalvarezgo.socikutxa.shared.infrastructure.monitoring.kamon

import scala.concurrent.{ExecutionContext, Future}
import cats.Monad
import cats.implicits._
import com.mikelalvarezgo.socikutxa.shared.domain.contract.monitoring.Histogram
import kamon.Kamon
import kamon.tag.TagSet

final class KamonHistogram(name: String, tags: Map[String, String] = Map.empty) extends Histogram {
  private val kamonHistogram = Kamon.histogram(name).withTags(TagSet.from(tags))

  override def record(value: Long): Unit =
    kamonHistogram.record(value): Unit

  override def record[T](codeToBenchmark: => T): T = {
    val startTime = System.nanoTime()
    val result    = codeToBenchmark
    val endTime   = System.nanoTime()

    record(endTime - startTime)
    result
  }

  override def recordFuture[T](
      codeToBenchmark: => Future[T]
  )(implicit ec: ExecutionContext): Future[T] = {
    val startTime = System.nanoTime()
    val result    = codeToBenchmark

    result.foreach(
        _ => record(System.nanoTime() - startTime)
    )
    result
  }

  override def recordFutureOnEitherRight[P[_] : Monad, T, U](
      codeToBenchmark: => P[Either[T, U]]
  ): P[Either[T, Unit]] = {
    val startTime = System.nanoTime()
    val result    = codeToBenchmark

    result.map(
        _.map(
            _ => record(System.nanoTime() - startTime)
        )
    )
  }
}
