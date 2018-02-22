package com.alexdupre.coinpayments

import play.api.libs.json._

package object models {

  def enumReads[E <: Enumeration](enum: E): Reads[E#Value] =
    (json: JsValue) =>
      json match {
        case JsString(s) => {
          try {
            JsSuccess(enum.withName(s))
          } catch {
            case _: NoSuchElementException =>
              JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
          }
        }
        case JsBoolean(b) => {
          try {
            JsSuccess(enum.withName(b.toString()))
          } catch {
            case _: NoSuchElementException =>
              JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$b'")
          }
        }
        case _ => JsError("String value expected")
    }

}
