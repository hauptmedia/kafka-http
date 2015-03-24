package de.hauptmedia.kafkahttp

import com.datastax.spark.connector.embedded.EmbeddedKafka
import com.typesafe.config.ConfigFactory
import de.hauptmedia.ConfigUtil._
import de.hauptmedia.http.Connectors._
import de.hauptmedia.http.Driver._
import de.hauptmedia.http.Handlers._
import de.hauptmedia.http.Requests._
import de.hauptmedia.http.Responders._
import kafka.consumer.{Consumer, ConsumerConfig}
import kafka.producer.{KeyedMessage, Producer, ProducerConfig}
import kafka.serializer.StringDecoder
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods._

import scala.collection.mutable


object RestServer {
  implicit val formats = DefaultFormats
  implicit val contentType = ContentType("application/json")

  val config                      = ConfigFactory.load

  val topics                      = config.getString("topics").split(",").filter( p => !p.isEmpty )

  val producerProperties          = toProperties(config.getConfig("producer"))
  val consumerProperties          = toProperties(config.getConfig("consumer"))

  val useEmbeddedProducerEndpoint = producerProperties.getProperty("metadata.broker.list")  == "embedded"
  val useEmebddedConsumerEndpoint = consumerProperties.getProperty("zookeeper.connect")     == "embedded"

  println("Starting HTTP listener at " + config.getString("http.host") + ":" + config.getInt("http.port"))
  listen(config.getString("http.host"), config.getInt("http.port"))

  if (topics.size == 0) {
    println("No topics specified. Please configure the topics in your configuration. Exiting now!")
    System.exit(-1)
  }

  println("Configured topics:")
  println
  println

  topics foreach {
    t => println("http://" + config.getString("http.host") + ":" + config.getInt("http.port") + "/topic/" + t + "/")
  }

  println
  println


  val kafkaTopicMap   = mutable.Map[String, Int]()
  topics foreach {
    t => kafkaTopicMap += t -> 1
  }

  if(useEmbeddedProducerEndpoint || useEmebddedConsumerEndpoint){
    val kafka = new EmbeddedKafka

    if(useEmbeddedProducerEndpoint) {
      producerProperties.setProperty("metadata.broker.list", kafka.kafkaConfig.hostName + ":" + kafka.kafkaConfig.port)
    }

    if(useEmebddedConsumerEndpoint) {
      consumerProperties.setProperty("zookeeper.connect", kafka.kafkaParams.get("zookeeper.connect").get)
    }
  }

  val producer        = new Producer[String, String](new ProducerConfig(producerProperties))
  val consumer        = Consumer.create(new ConsumerConfig(consumerProperties))
  val consumerStream  = consumer.createMessageStreams[String, String](
    kafkaTopicMap,
    new StringDecoder(),
    new StringDecoder()
  )

  topics foreach {
    topic => get("/topic/" + topic) {
      request => {
        val limit = (request("limit") getOrElse "1").toInt
        var data = new mutable.ListBuffer[Map[String, String]]

        try {
          synchronized {
            consumerStream.get(topic).get(0).slice(0, limit) foreach {
              item => data += Map("key" -> item.key, "message" -> item.message)
            }
          }
        } catch {
          case e:kafka.consumer.ConsumerTimeoutException => {}
        }

        toJsonResponse(data.toSeq)
      }

    }

      post("/topic/" + topic) {
        request =>
          if(request("data").isEmpty) {
            toJsonResponse(Map(
              "rc"  -> "500",
              "msg" -> "missing data parameter"
            ))

          } else {

            try {
              parse(request("data").get).extract[List[Map[String, String]]] foreach {
                m => producer.send(new KeyedMessage[String, String](topic, m.get("key").get, m.get("message").get))
              }

              "OK"
            } catch {

              case e: Exception => toJsonResponse(Map(
                "rc"  -> "500",
                "msg" -> e.toString
              ))

            }
          }

      }

  }


  def main(args: Array[String]) = { start; join }
}