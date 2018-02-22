package com.alexdupre.coinpayments

abstract class CoinPaymentsException(message: String) extends Exception(message)

case class CoinPaymentsProtocolException(error: String = "CoinPayments Protocol Error") extends CoinPaymentsException(error)

case class CoinPaymentsReturnedException(error: String) extends CoinPaymentsException(error)

case class CoinPaymentsValidationException(error: String) extends CoinPaymentsException(error)
