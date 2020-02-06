package com.alexdupre.coinpayments.models

import java.time.Instant

import Status.Status
import Capability.Capability
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class CoinInfo(
    isFiat: Boolean,
    rateBTC: BigDecimal,
    lastUpdate: Instant,
    txFee: BigDecimal,
    status: Status,
    name: String,
    confirms: Int,
    conConvert: Boolean,
    capabilities: Seq[Capability],
    accepted: Boolean
)

object CoinInfo {

  implicit val read: Reads[CoinInfo] = (
    (__ \ "is_fiat").read[Int].map(_ == 1) and
      (__ \ "rate_btc").read[BigDecimal] and
      (__ \ "last_update")
        .read[BigDecimal]
        .map(v => Instant.ofEpochSecond(v.longValue)) and
      (__ \ "tx_fee").read[BigDecimal] and
      (__ \ "status").read[Status] and
      (__ \ "name").read[String] and
      (__ \ "confirms").read[BigDecimal].map(_.intValue) and
      (__ \ "can_convert").read[Int].map(_ == 1) and
      (__ \ "capabilities").read[Seq[Capability]] and
      (__ \ "accepted").read[Int].map(_ == 1)
  )(CoinInfo.apply _)
}
