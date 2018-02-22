package com.alexdupre.coinpayments.models

object Status extends Enumeration {
  type Status = Value

  val Online      = Value("online")
  val Maintenance = Value("maintenance")

  implicit val read = enumReads(Status)
}
