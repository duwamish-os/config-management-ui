package configuration.db

import java.io._

import org.iq80.leveldb._
import org.iq80.leveldb.impl.Iq80DBFactory._

object ConfigDatabase {

  val database: DB = factory.open(new File("templates"), new Options().createIfMissing(true))

  database.put(bytes("order received"), bytes("your order has been received and will be shipped in a while."))
  database.put(bytes("order shipped"), bytes("your order has been shipped."))

  def lookup(key: String): String = {
    asString(database.get(bytes("order received")))
  }

  def update(key: String, value: String): Unit = {

    database.delete(bytes(key))

    database.put(bytes(key), bytes(value))
  }
}
