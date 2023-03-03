package com.alexdupre.coinpayments.models

import play.api.libs.json.Reads

object Status extends Enumeration {
  type Status = Value

  val Online      = Value("online")
  val Maintenance = Value("maintenance")

  implicit val read: Reads[Status.Value] = enumReads(Status)
}
