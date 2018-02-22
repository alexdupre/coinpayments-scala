package com.alexdupre.coinpayments

import com.alexdupre.coinpayments.models._

import scala.concurrent.Future

trait CoinPayments {

  def getCoinsWithRates(): Future[Map[String, CoinInfo]]

  def createInvoice(amount: BigDecimal,
                    currency: String,
                    crypto: String,
                    buyerEmail: Option[String] = None,
                    buyerName: Option[String] = None,
                    itemName: Option[String] = None,
                    itemNumber: Option[String] = None,
                    invoice: Option[String] = None,
                    custom: Option[String] = None,
                    ipnUrl: Option[String] = None): Future[Invoice]

  def getCallbackAddress(currency: String, ipnUrl: Option[String]): Future[Address]

}
