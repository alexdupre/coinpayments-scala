package com.alexdupre.coinpayments.models

import play.api.libs.json.Reads

object Capability extends Enumeration {
  type Capability = Value

  val Payments       = Value("payments")
  val DestinationTag = Value("dest_tag")
  val Transfers      = Value("transfers")
  val Wallet         = Value("wallet")
  val Convert        = Value("convert")

  implicit val read: Reads[Capability.Value] = enumReads(Capability)

}
