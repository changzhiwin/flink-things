name := "Flink Practice"
organization := "bigdata.flink"
version := "1.1.0"
scalaVersion := "2.12.17"

val flinkVersion = "1.16.0"

libraryDependencies ++= Seq(
  "org.apache.flink" % "flink-streaming-java" % flinkVersion % "provided",
)

Compile / mainClass := Some("bigdata.flink.things.AverageSensorReadings")

Compile / scalacOptions ++= List("-feature", "-deprecation", "-unchecked", "-Xlint")