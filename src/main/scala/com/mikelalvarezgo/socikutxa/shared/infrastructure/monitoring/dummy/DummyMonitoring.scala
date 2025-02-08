package com.mikelalvarezgo.socikutxa.shared.infrastructure.monitoring.dummy

import com.mikelalvarezgo.socikutxa.shared.domain.contract.monitoring.{
  Counter,
  Gauge,
  Histogram,
  Monitoring
}

object DummyMonitoring extends Monitoring {
  override def gauge(name: String, tags: Map[String, String]): Gauge         = DummyGauge
  override def counter(name: String, tags: Map[String, String]): Counter     = DummyCounter
  override def histogram(name: String, tags: Map[String, String]): Histogram = DummyHistogram

  object DummyCounter extends Counter {
    override def increment(): Unit   = ()
    override def add(num: Int): Unit = ()
  }

  object DummyGauge extends Gauge {
    override def increment(): Unit        = ()
    override def decrement(): Unit        = ()
    override def add(num: Int): Unit      = ()
    override def subtract(num: Int): Unit = ()
  }

  object DummyHistogram extends Histogram {
    override def record(value: Long): Unit = ()
  }
}
