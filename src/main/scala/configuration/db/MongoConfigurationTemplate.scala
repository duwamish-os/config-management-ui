package configuration.db

import com.mongodb.BasicDBObject
import com.mongodb.casbah.commons.TypeImports._
import com.mongodb.casbah.{MongoClient, commons}
import com.mongodb.casbah.commons.MongoDBObject


object MongoConfigurationTemplate {

  val mongoClient = MongoClient("localhost", 27017)
  val db = mongoClient("ConfigDb")
  val messagingTemplateCollection = db("MessagingTemplate")

  def addConfig(statusKey: String, statusValue: String): Boolean = {
    val write = messagingTemplateCollection.insert(
      MongoDBObject(
        "food_type" -> "delivery",
        "messaging" -> MongoDBObject(
          "food_status" -> statusKey,
          "messaging" -> statusValue
        )
      )
    )

    write.getN == 1
  }

  def getConfig(keyStatus: String): Option[String] = {
    val result: Option[DBObject] = messagingTemplateCollection.findOne(MongoDBObject("messaging.food_status" -> keyStatus),
      MongoDBObject("messaging.messaging" -> 1))

    result.map {
      case r: BasicDBObject => r.get("messaging").asInstanceOf[BasicDBObject].getString("messaging")
    }
  }
}
