package com.alexdupre.coinpayments.models

import java.net.URLDecoder

import com.alexdupre.coinpayments.CoinPaymentsValidationException
import org.apache.commons.codec.digest.{HmacAlgorithms, HmacUtils}

trait IPN {
  val id: String
}

case class DepositNotification(
    id: String,
    address: String,
    txnId: String,
    status: Int,
    statusText: String,
    currency: String,
    confirms: Int,
    amount: BigDecimal,
    amounti: BigInt,
    fee: Option[BigDecimal], // only sent when status >= 100
    feei: Option[BigInt],    // only sent when status >= 100
    destTag: Option[String]  // Ripple Destination Tag, Monero Payment ID, etc.
) extends IPN

object IPN {

  def validate(merchantId: String, ipnSecret: String, data: Array[Byte], hmac: String): IPN = {

    val params = new String(data, "UTF-8")
      .split('&')
      .map { param =>
        val parts = param.split("=", -1)
        val key   = URLDecoder.decode(parts(0), "UTF-8")
        val value = URLDecoder.decode(parts.lift(1).getOrElse(""), "UTF-8")
        key -> value
      }
      .toMap

    if (!params.get("ipn_version").contains("1.0"))
      throw CoinPaymentsValidationException("IPN Version is not 1.0")

    if (!params.get("ipn_mode").contains("hmac"))
      throw CoinPaymentsValidationException("IPN Mode is not HMAC")

    if (!params.get("merchant").contains(merchantId))
      throw CoinPaymentsValidationException("No or incorrect Merchant ID passed")

    if (!hmac.equalsIgnoreCase(new HmacUtils(HmacAlgorithms.HMAC_SHA_512, ipnSecret).hmacHex(data)))
      throw CoinPaymentsValidationException("HMAC signature does not match")

    params.get("ipn_type") match {
      case Some("deposit") =>
        DepositNotification(
          params("ipn_id"),
          params("address"),
          params("txn_id"),
          params("status").toInt,
          params("status_text"),
          params("currency"),
          params("confirms").toInt,
          BigDecimal(params("amount")),
          BigInt(params("amounti")),
          params.get("fee").map(BigDecimal.apply),
          params.get("feei").map(BigInt.apply),
          params.get("dest_tag")
        )
      case _ =>
        throw CoinPaymentsValidationException("No or unsupported IPN type passed")
    }
  }
}
