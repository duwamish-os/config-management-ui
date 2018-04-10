package configuration.ui

import java.util.UUID

import configuration.db.ConfigDatabase
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ConfigurationServer extends ScalatraServlet with JacksonJsonSupport {

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

  post("/chat") {

    val r = request.body
    logger.info(r)

    val chatReq = parse(r).extract[ChatRequest]

    response.setStatus(200)

    ChatResponse(chatReq.correlationID, List(ChatDisplayCard("Hello, how can I help you?")))
  }

}

//TODO add UUID deserializer
case class ChatRequest(correlationID: String, message: String)

case class ChatResponse(correlationID: String, displayCards: List[ChatDisplayCard])

case class ChatDisplayCard(displayText: String)

object ConfigurationServer {
  val logger: Logger = LoggerFactory.getLogger(classOf[ConfigurationServer])
}
