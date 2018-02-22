# coinpayments-scala

[![Build Status](https://travis-ci.org/alexdupre/coinpayments-scala.png?branch=master)](https://travis-ci.org/alexdupre/coinpayments-scala)

An asynchronous / non-blocking Scala library for the CoinPayments API.

Currently there is full support for the Callback Addresses (IPN included) and partial support for Create Transaction API (no IPN).

## Artifacts

The latest release of the library is compiled with Scala 2.12 and supports only Gigahorse with OkHttp backend as HTTP provider.

| Version | Artifact Id             | HTTP Provider   | Play-JSON | Scala       |
| ------- | ----------------------- | --------------- | --------- | ----------- |
| 1.0     | coinpayments            | Gigahorse 0.3.x | 2.6.x     | 2.12 Only   |

If you're using SBT, add the following line to your build file:

```scala
libraryDependencies += "com.alexdupre" %% "coinpayments" % "<version>"
```

## Usage

The [CoinPayments](https://github.com/alexdupre/coinpayments-scala/blob/master/src/main/scala/com/alexdupre/coinpayments/CoinPayments.scala) trait
contains all the public methods that can be called on the client object.

Eg. if you like to create a new LTCT deposit address you can use the following code snippet:

```scala
import com.alexdupre.coinpayments._
import com.alexdupre.coinpayments.models._

val pubKey = ...
val privKey = ...

val client: CoinPayments = CoinPaymentsClient(pubKey, privKey)

val address: Future[Address] = client.getCallbackAddress("LTCT")
```
