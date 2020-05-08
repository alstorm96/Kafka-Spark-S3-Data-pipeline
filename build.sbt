name := "demo"

version := "0.1"

scalaVersion := "2.11.12"


libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.5"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.5" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.5" % "provided"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.5"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.5"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.4.1"
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.10.47" exclude("com.fasterxml.jackson.core", "jackson-databind")

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.startsWith("META-INF") => MergeStrategy.discard
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList("org", "apache", xs@_*) => MergeStrategy.first
  case "about.html" => MergeStrategy.rename
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}

mainClass in assembly := Some("GetStreamingDepartmentTraffic")






