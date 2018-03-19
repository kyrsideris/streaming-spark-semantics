name := "kafka-processor"

mainClass in (Compile, run) := Some("com.meetup.sparksouthwest.KafkaProcessor")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
