package com.mikelalvarezgo.socikutxa.shared.domain.contract.monitoring

trait Counter {
  def increment(): Unit
  def add(num: Int): Unit
}
