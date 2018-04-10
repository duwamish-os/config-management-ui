package configuration.ui

import configuration.db.ConfigDatabase
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.{ExecutionContext, Future}

class ConfigurationServer extends ScalatraServlet with JacksonJsonSupport with FutureSupport {

  import ConfigurationServer._

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    "config management"
  }

  get("/config") {
    Map("config" -> ConfigDatabase.lookup("order received"))
  }

  get("/config_non_blocking") {
    Future {
      Map("config" -> ConfigDatabase.lookup("order received"))
    }
  }

  case class User(id: String, accessToken: String)

  post("/chat") {

    new AsyncResult() {
      override val is: Future[ApiResponse] = Future {
        val r = request.body
        logger.info(r)

        val userID = Option(request.getHeader("userID"))
        val accessToken = Option(request.getHeader("accessToken"))

        userID match {
          case Some(a) =>
            val chatReq = parse(r).extract[ChatRequest]

            response.setStatus(200)

            ChatResponse(chatReq.correlationID, List(ChatDisplayCard("Hello, how can I help you?")))

          case None => errorHandling(ApiException("ValidationError", "UserID is required"))
        }
      }
    }
  }

  val errorHandling: PartialFunction[Throwable, ApiError] = {
    case api: ApiException => ApiError(api.errorCode, api.errorMessage)
    case a => ApiError("UnknownError", a.getMessage)
  }

  override protected implicit def executor: ExecutionContext = Implicits.global
}

//TODO add UUID deserializer
case class ChatRequest(correlationID: String, message: String)

case class ChatResponse(correlationID: String, displayCards: List[ChatDisplayCard]) extends ApiResponse

case class ChatDisplayCard(displayText: String)

trait ApiResponse

case class ApiException(errorCode: String, errorMessage: String) extends RuntimeException(errorMessage)

case class ApiError(errorCode: String, errorMessage: String) extends ApiResponse

object ConfigurationServer {
  val logger: Logger = LoggerFactory.getLogger(classOf[ConfigurationServer])
}
