package de.hauptmedia

import com.typesafe.config.Config
import scala.collection.JavaConverters.asScalaSetConverter

object ConfigUtil {
  /** return a java.util.Properties from a config paragraph.  All keys in the
    * selected config paragraph are interpreted as strings
    */
  def toProperties(config: Config): java.util.Properties = {
    val entries =
      config.entrySet.asScala.map { entry =>
        val key = entry.getKey
        val value = config.getString(key)
        (key, value)
      }
    val properties = new java.util.Properties()
    entries.foreach { case (key, value) => properties.put(key, value) }
    properties
  }
}
