package configuration.ui

import configuration.db.ConfigDatabase
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._

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

}
