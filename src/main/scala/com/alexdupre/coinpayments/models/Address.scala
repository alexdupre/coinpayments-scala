package com.alexdupre.coinpayments.models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Address(address: String, pubkey: Option[String], destTag: Option[String])

object Address {

  implicit val read: Reads[Address] = (
    (__ \ "address").read[String] and
      (__ \ "pubkey").readNullable[String] and
      (__ \ "dest_tag")
        .readNullable[JsValue]
        .map(_.flatMap {
          case JsString(v) => Some(v)
          case JsNumber(v) => Some(v.underlying().toPlainString)
          case _           => None
        })
  )(Address.apply _)

}
