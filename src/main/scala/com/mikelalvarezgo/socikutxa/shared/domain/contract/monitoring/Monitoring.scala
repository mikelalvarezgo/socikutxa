package com.mikelalvarezgo.socikutxa.shared.domain.contract.monitoring

trait Monitoring {
  def counter(name: String, tags: Map[String, String] = Map.empty): Counter
  def histogram(name: String, tags: Map[String, String] = Map.empty): Histogram
  def gauge(name: String, tags: Map[String, String] = Map.empty): Gauge
}
