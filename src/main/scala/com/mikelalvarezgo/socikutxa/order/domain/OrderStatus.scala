package com.mikelalvarezgo.socikutxa.order.domain

sealed trait OrderStatus

object OrderStatus {
  case object Draft       extends OrderStatus
  case object InProgress  extends OrderStatus
  case object Completed   extends OrderStatus
  case object Cancelled   extends OrderStatus

  def fromString(value: String): Option[OrderStatus] = value.toUpperCase match {
    case "DRAFT"       => Some(Draft)
    case "IN_PROGRESS" => Some(InProgress)
    case "COMPLETED"   => Some(Completed)
    case "CANCELLED"   => Some(Cancelled)
    case _             => None
  }

  def toString(status: OrderStatus): String = status match {
    case Draft      => "DRAFT"
    case InProgress => "IN_PROGRESS"
    case Completed  => "COMPLETED"
    case Cancelled  => "CANCELLED"
  }
}
