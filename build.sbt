name := "kafka-http"

version := "0.1"

scalaVersion := "2.10.4"

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "log4j"                 % "log4j"        % "1.2.17",
  "org.eclipse.jetty"      % "jetty-server" % "8.1.16.v20140903",
  "org.eclipse.jetty"      % "jetty-servlet" % "8.1.16.v20140903",
  "org.json4s"            %% "json4s-native" % "3.2.11",
  "org.apache.kafka"       % "kafka_2.10" % "0.8.2.1",
  "com.typesafe"          % "config"        % "1.2.1",
  "com.datastax.spark" %% "spark-cassandra-connector-embedded" % "1.2.0-rc1"
)

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case _                                                   => MergeStrategy.first
}