package com.alexdupre.coinpayments

import com.alexdupre.coinpayments.models._
import gigahorse.HttpWrite.utf8
import gigahorse._
import gigahorse.support.okhttp.Gigahorse._
import org.apache.commons.codec.digest.{HmacAlgorithms, HmacUtils}
import org.slf4j.LoggerFactory
import play.api.libs.json._

import scala.collection.immutable.TreeMap
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.{postfixOps, reflectiveCalls}
import scala.util.control.NonFatal

class CoinPaymentsClient(pubKey: String, privKey: String, http: HttpClient)(implicit ec: ExecutionContext) extends CoinPayments {

  val logger = LoggerFactory.getLogger(classOf[CoinPaymentsClient])

  val apiUrl = "https://www.coinpayments.net/api.php"

  implicit private val httpWrite: HttpWrite[TreeMap[String, String]] =
    new HttpWrite[TreeMap[String, String]] {
      import java.net.URLEncoder
      def toByteArray(formData: TreeMap[String, String]): Array[Byte] =
        formData
          .map { case (k, v) =>
            k + "=" + URLEncoder.encode(v, "UTF-8")
          }
          .mkString("&")
          .getBytes(utf8)
      def contentType: Option[String] = Some(ContentTypes.FORM)
    }

  object XSignatureCalculator extends SignatureCalculator {
    override def sign(url: String, contentType: Option[String], content: Array[Byte]) =
      ("HMAC", new HmacUtils(HmacAlgorithms.HMAC_SHA_512, privKey).hmacHex(content))
  }

  private def addAuthentication(req: Request): Request = {
    req.withSignatureOpt(Some(XSignatureCalculator))
  }

  private def normalizeParams(params: Map[String, Option[Any]]): TreeMap[String, String] = {
    TreeMap(params.toSeq.map {
      case (key, Some(value)) => Some(key -> value.toString)
      case _                  => None
    }.flatten: _*)
  }

  private def post(cmd: String, params: Map[String, Option[Any]]) = {
    val fullParams = normalizeParams(params) + ("version" -> "1") + ("key" -> pubKey) + ("cmd" -> cmd)
    val req        = url(apiUrl).withSignatureOpt(Some(XSignatureCalculator))
    req.post(fullParams)
  }

  private def execute[T](req: Request)(implicit fjs: Reads[T]): Future[T] = {
    logger.debug(s"Request: ${req.method} ${req.url}")
    if (req.method == "POST" && logger.isTraceEnabled)
      logger.trace(s"Payload: ${new String(req.body.asInstanceOf[InMemoryBody].bytes, utf8)}")
    http.processFull(req).map { response =>
      logger.debug(s"Response Status: ${response.status}")
      val js =
        try {
          Json.parse(response.bodyAsString)
        } catch {
          case NonFatal(_) => throw CoinPaymentsProtocolException()
        }
      logger.trace(s"Response:\n${Json.prettyPrint(js)}")
      (js \ "error").validate[String] match {
        case JsSuccess(value, _) if value == "ok" => (js \ "result").as[T]
        case JsSuccess(error, _) =>
          throw new CoinPaymentsReturnedException(error)
        case _ => throw new CoinPaymentsProtocolException()
      }
    }
  }

  override def getCoinsWithRates(): Future[Map[String, CoinInfo]] = {
    val params = Map("accepted" -> Some(1))
    execute[Map[String, CoinInfo]](post("rates", params))
  }

  override def createInvoice(
      amount: BigDecimal,
      currency: String,
      crypto: String,
      buyerEmail: Option[String],
      buyerName: Option[String],
      itemName: Option[String],
      itemNumber: Option[String],
      invoice: Option[String],
      custom: Option[String],
      ipnUrl: Option[String]
  ): Future[Invoice] = {
    val params = Map(
      "amount"      -> Some(amount),
      "currency1"   -> Some(currency),
      "currency2"   -> Some(crypto),
      "buyer_email" -> buyerEmail,
      "buyer_name"  -> buyerName,
      "item_name"   -> itemName,
      "item_number" -> itemNumber,
      "invoice"     -> invoice,
      "custom"      -> custom,
      "ipn_url"     -> ipnUrl
    )
    execute[Invoice](post("create_transaction", params))
  }

  override def getCallbackAddress(currency: String, ipnUrl: Option[String]): Future[Address] = {
    val params = Map(
      "currency" -> Some(currency),
      "ipn_url"  -> ipnUrl
    )
    execute[Address](post("get_callback_address", params))
  }

}

object CoinPaymentsClient {

  def apply(pubKey: String, privKey: String)(implicit ec: ExecutionContext) =
    new CoinPaymentsClient(pubKey, privKey, defaultHttpExecutor)

  lazy val defaultHttpExecutor = {
    val cfg = config
      .withUserAgentOpt(Some(s"AlexDupre-CoinPayments/${BuildInfo.version}"))
      .withConnectTimeout(5 seconds)
      .withReadTimeout(30 seconds)
      .withRequestTimeout(60 seconds)
    http(cfg)
  }

}
