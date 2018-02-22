package com.alexdupre.coinpayments.models

object Capability extends Enumeration {
  type Capability = Value

  val Payments       = Value("payments")
  val DestinationTag = Value("dest_tag")
  val Transfers      = Value("transfers")
  val Wallet         = Value("wallet")
  val Convert        = Value("convert")

  implicit val read = enumReads(Capability)

}
