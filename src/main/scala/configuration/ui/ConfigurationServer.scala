package configuration.ui

import configuration.db.ConfigDatabase
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ConfigurationServer extends ScalatraServlet with JacksonJsonSupport {

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
}
