# streaming-spark-semantics

## Structure

This sbt build two projects: `kafka-processor` and `kafka-generator`.
Kafka processor is a Spark application that reads from Kafka and Kafka generator is a Scala application that produces
to messages to Kafka asynchronously.

The project includes a Docker compose file to test the producer and consumer.
 
## Testing

- Build the two jars: `sbt clean assembly`
- Bring up zookeeper: `docker-compose -f docker/testing.yml up zookeeper`
- Bring up kafka: `docker-compose -f docker/testing.yml up kafka`
- Bring up processor: `docker-compose -f docker/testing.yml up processor`
- Bring up generator: `docker-compose -f docker/testing.yml up generator`

## Semantics

From Spark documentation: 
The semantics of streaming systems are often captured in terms of how many times each record can be processed
by the system. There are three types of guarantees that a system can provide under all possible operating
conditions (despite failures, etc.)

- At most once: Each record will be either processed once or not processed at all.
- At least once: Each record will be processed one or more times.
    This is stronger than at-most once as it ensure that no data will be lost. But there may be duplicates.
- Exactly once: Each record will be processed exactly once - no data will be lost and no data will be
    processed multiple times. This is obviously the strongest guarantee of the three.

## Streaming in Spark

### Spark Streaming

Spark streaming is also know as `DStreams`. Documentation is describing the fault tolerance semantics:
https://spark.apache.org/docs/2.3.0/streaming-programming-guide.html#fault-tolerance-semantics

### Structured Streaming

End-to-end exactly-once semantics in structure streaming:
https://spark.apache.org/docs/2.3.0/structured-streaming-programming-guide.html#fault-tolerance-semantics
