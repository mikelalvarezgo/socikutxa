package com.mikelalvarezgo.socikutxa.shared.infrastructure.monitoring.kamon

import com.mikelalvarezgo.socikutxa.shared.domain.contract.monitoring.Counter
import kamon.Kamon
import kamon.tag.TagSet

final class KamonCounter(name: String, tags: Map[String, String] = Map.empty) extends Counter {
  private val kamonCounter = Kamon.counter(name).withTags(TagSet.from(tags))

  override def increment(): Unit   = kamonCounter.increment(): Unit
  override def add(num: Int): Unit = kamonCounter.increment(num): Unit
}
