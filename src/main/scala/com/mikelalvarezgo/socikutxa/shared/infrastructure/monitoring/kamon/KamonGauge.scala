package com.mikelalvarezgo.socikutxa.shared.infrastructure.monitoring.kamon

import com.mikelalvarezgo.socikutxa.shared.domain.contract.monitoring.Gauge
import kamon.Kamon
import kamon.tag.TagSet

final class KamonGauge(name: String, tags: Map[String, String] = Map.empty) extends Gauge {
  private val kamonGauge = Kamon.gauge(name).withTags(TagSet.from(tags))

  override def increment(): Unit        = kamonGauge.increment(): Unit
  override def decrement(): Unit        = kamonGauge.decrement(): Unit
  override def add(num: Int): Unit      = kamonGauge.increment(num): Unit
  override def subtract(num: Int): Unit = kamonGauge.decrement(num): Unit
}
