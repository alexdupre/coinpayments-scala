package com.alexdupre.coinpayments.models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Invoice(
    amount: BigDecimal,
    address: String,
    txnId: String,
    confirmsNeeded: Int,
    timeout: Int,
    statusUrl: String,
    qrcodeUrl: String,
    destTag: Option[String]
)

object Invoice {

  implicit val readInvoice: Reads[Invoice] = (
    (__ \ "amount").read[BigDecimal] and
      (__ \ "address").read[String] and
      (__ \ "txn_id").read[String] and
      (__ \ "confirms_needed").read[BigDecimal].map(_.intValue) and
      (__ \ "timeout").read[Int] and
      (__ \ "status_url").read[String] and
      (__ \ "qrcode_url").read[String] and
      (__ \ "dest_tag")
        .readNullable[JsValue]
        .map(_.flatMap {
          case JsString(v) => Some(v)
          case JsNumber(v) => Some(v.underlying().toPlainString)
          case _           => None
        })
  )(Invoice.apply _)

}
