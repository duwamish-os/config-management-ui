package configuration.ui

import configuration.db.{LevelDbConfigurationTemplate, MongoConfigurationTemplate}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.{AsyncResult, FutureSupport, ScalatraServlet}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits
import scala.concurrent.duration.{Duration, _}
import scala.concurrent.{ExecutionContext, Future}

class ConfigurationServer extends ScalatraServlet with JacksonJsonSupport with FutureSupport {

  import ConfigurationServer._

  protected implicit val jsonFormats: Formats = DefaultFormats

  protected implicit def executor: ExecutionContext = Implicits.global

  before() {
    contentType = formats("json")
  }

  get("/") {
    "config management"
  }

  get("/config/update") {
    Map("config updated" -> MongoConfigurationTemplate.addConfig("order received", "We just received your order for food. Stay tuned for delivery."))
  }

  get("/config/list") {
    Future {
      Map("config" -> MongoConfigurationTemplate.getConfig("order received"))
    }
  }

  case class User(id: String, accessToken: String)

  def chatReqValidation: ((String, String) => Either[ApiException, Boolean]) = (userID, accessToken) =>
    if (userID == null) Left(ApiException("ValidationError", "UserID is required"))
    else if (accessToken == null) Left(ApiException("ValidationError", "accessToken is required"))
    else Right(true)

  post("/chat") {

    new AsyncResult() {
      override val is: Future[ApiResponse] = Future {
        val reqPayload = request.body
        logger.info("ChatRequest:" + reqPayload)

        chatReqValidation(request.getHeader("userID"), request.getHeader("accessToken")) match {
          case Left(a) => errorHandling(a)
          case Right(_) =>
            val chatReq = parse(reqPayload).extract[ChatRequest]

            response.setStatus(200)

            ChatResponse(chatReq.correlationID, List(ChatDisplayCard("Hello, how can I help you?")))
        }
      }

      override implicit def timeout: Duration = 15 seconds
    }
  }

  val errorHandling: PartialFunction[Throwable, ApiError] = {
    case api: ApiException => ApiError(api.errorCode, api.errorMessage)
    case a => ApiError("UnknownError", a.getMessage)
  }
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
