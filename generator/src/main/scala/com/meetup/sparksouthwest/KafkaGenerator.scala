package com.meetup.sparksouthwest

import java.util.Properties

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import scala.util.Random
import scala.math._

object KafkaGenerator {

  def main(args: Array[String]): Unit = {

    val topic = "noisy-sinusoid"
    val brokers = "kafka:9092"
    val rnd = new Random()
    val props = new Properties()
    props.put("bootstrap.servers", brokers)
    props.put("client.id", "NoisySinusoid")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.DoubleSerializer")

    val producer = new KafkaProducer[String, Double](props)

    val duration = 5 * 60 * 1000 // 5 mins in millis
    val startTime = System.currentTimeMillis()
    val finishTime = startTime + duration

    val count: Long = 0L
    while (System.currentTimeMillis() < finishTime) {
      val key = ""
      // 2 * pi in 12 seconds, 5 cycles a minute
      val value = sin( Pi * ((System.currentTimeMillis() / 1000.0D) % 12) / 6 )

      val data = new ProducerRecord[String, Double](topic, key, value)

      //async
      producer.send(data,
                    new Callback() {
                      def onCompletion(metadata: RecordMetadata, e: Exception) {
                        if(e != null) {
                          e.printStackTrace()
                          println("The offset of the record we just sent is: " + metadata.offset())
                        }
                      }
                    })
      //sync
      //producer.send(data)
      Thread.sleep(1) // sleep one minisecond
    }

    println(s"Send: '$count', sent per second: '${count / (System.currentTimeMillis() - startTime)}'")

    producer.close()
  }
}
