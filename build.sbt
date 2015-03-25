name := "kafka-http"

version := "0.1"

scalaVersion := "2.10.4"

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "log4j"               % "log4j"        % "1.2.17",
  "org.eclipse.jetty"   % "jetty-server" % "8.1.16.v20140903",
  "org.eclipse.jetty"   % "jetty-servlet" % "8.1.16.v20140903",
  "org.mortbay.jetty"   % "jetty" % "6.1.26",
  "org.json4s"            %% "json4s-native" % "3.2.11",
  "org.apache.kafka"    % "kafka_2.10" % "0.8.2.1",
  "com.typesafe"        % "config"        % "1.2.1",
  "com.google.guava"    % "guava" % "18.0",
  "org.apache.commons"  % "commons-lang3" % "3.3.2",
  "com.typesafe.akka"   % "akka-actor_2.10" % "2.3.9"
)

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case _                                                   => MergeStrategy.first
}