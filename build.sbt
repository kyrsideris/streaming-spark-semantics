import sbt._, Keys._

name := "kafka-root"

version := "1.0.0"
scalaVersion := "2.11.12"
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

lazy val processor = project.
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.processorDependencies)

lazy val generator = project.
  settings(Common.settings: _*).
  settings(libraryDependencies ++= Dependencies.generatorDependencies)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

//lazy val commonSettings = Seq(
//  organization := "com.meetup.sparksouthwest",
//  version := "1.0",
//  scalaVersion := "2.11.12",
//  assemblyMergeStrategy in assembly := {
//    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//    case x => MergeStrategy.first
//  }
//)
//
////lazy val root = (project in file("projects/root")) // .aggregate(processor, generator)
////  .settings(commonSettings: _*)
//
//lazy val processor = (project in file("processor"))
//  .settings(commonSettings: _*)
//  .settings(
//    name := "processor",
//    mainClass in assembly := Some("KafkaProcessor"),
//    libraryDependencies ++= Seq(
//      "org.apache.spark" %% "spark-core" % "2.3.0",
//      "org.apache.spark" %% "spark-sql" % "2.3.0",
//      "org.apache.spark" %% "spark-streaming" % "2.3.0",
//      "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.3.0",       // for structured streaming
//      "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.3.0"  // for spark streaming
//    ),
//    scalaSource := baseDirectory.value / "src"
//  )
//
//lazy val generator = (project in file("generator"))
//  .settings(commonSettings: _*)
//  .settings(
//    name := "generator",
//    mainClass in assembly := Some("KafkaGenerator"),
//    libraryDependencies ++= Seq(
//      "org.apache.kafka" % "kafka-clients" % "0.10.0.1"
//    ),
//    scalaSource := baseDirectory.value / "src"
//  )
