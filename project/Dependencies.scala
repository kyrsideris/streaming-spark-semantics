import sbt._
import Keys._

object Dependencies {
  val processorDependencies: Seq[ModuleID] = Seq(
    "org.apache.spark" %% "spark-core" % "2.3.0",
    "org.apache.spark" %% "spark-sql" % "2.3.0",
    "org.apache.spark" %% "spark-streaming" % "2.3.0",
    "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.3.0",       // for structured streaming
    "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.3.0"  // for spark streaming
  )

  val generatorDependencies: Seq[ModuleID] = Seq(
    "org.apache.kafka" % "kafka-clients" % "0.10.0.1"
  )
}