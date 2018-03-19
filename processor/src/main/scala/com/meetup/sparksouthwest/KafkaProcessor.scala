/**
  * Created by kyriakos on 18/03/2018.
  */
package com.meetup.sparksouthwest

import java.util.Calendar

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.{DoubleDeserializer, StringDeserializer}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext, dstream}

object KafkaProcessor {

  type StreamingType = dstream.InputDStream[ConsumerRecord[String, Double]]

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("KafkaProcessor")
      .getOrCreate()

    val sc = spark.sparkContext
    val ssc = new StreamingContext(sc, Seconds(1))

    map(
      stream(
        ssc, brokers = "kafka:9092", group = "KafkaProcessor", List("noisy-sinusoid")
      )
    )

    ssc.start()
    ssc.awaitTermination()
  }

  private def stream(ssc: StreamingContext,
                     brokers: String,
                     group: String,
                     topics: Iterable[String]
                    ): StreamingType = {
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[DoubleDeserializer],
      "group.id" -> group,
      // "auto.offset.reset" -> "latest",
      // if: automatic management of Kafka offset
      // "enable.auto.commit" -> (true: java.lang.Boolean)
      // else: management of Kafka offset
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )

    KafkaUtils.createDirectStream[String, Double](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, Double](
        topics,       // list of topics
        kafkaParams   // kafka parameters
      )
    )
  }

  private def process(rdd: RDD[Double]): Unit = {
    val result = rdd.aggregate((0L, 0D))(
      { case ((count, sum), x) => (count + 1, sum + x) },
      { case ((count1, sum1), (count2, sum2)) => (count1 + count2, sum1 + sum2) }
    )

    val now = Calendar.getInstance.getTime

    println(s"$now: ${result._2 / result._1}")
  }

  def map(stream: StreamingType): Unit = {

    stream.foreachRDD {inputRdd =>
      val offsetRanges = inputRdd.asInstanceOf[HasOffsetRanges].offsetRanges
      val rdd = inputRdd.map(cr => cr.value).cache()

      process(rdd)

      stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
    }
  }

}
