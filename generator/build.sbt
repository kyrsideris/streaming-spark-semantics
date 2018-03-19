name := "kafka-generator"

mainClass in (Compile, run) := Some("com.meetup.sparksouthwest.KafkaGenerator")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
