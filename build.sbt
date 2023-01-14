name := "Flink Practice"
organization := "zhiwin.flink" // change to your org
version := "1.0.0"
scalaVersion := "2.12.17"

val flinkVersion = "1.16.0"

resolvers += "MavenRepository" at "https://mvnrepository.com/"

libraryDependencies ++= Seq(
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
  // "org.apache.flink" %% "flink-runtime-web" % flinkVersion % "provided",
  // "org.apache.flink" %% "flink-queryable-state-runtime" % flinkVersion % "provided",

  "org.scala-lang" % "scala-library" % "2.12.17" % "provided",
)

Compile / mainClass := Some("bigdata.flink.things.AverageSensorReadings")

Compile / scalacOptions ++= List("-feature", "-deprecation", "-unchecked", "-Xlint")