name := """rgts-service"""

version := "1.0"

scalaVersion := "2.11.6"

// Bring the sbt-aspectj settings into this build
aspectjSettings

// Here we are effectively adding the `-javaagent` JVM startup
// option with the location of the AspectJ Weaver provided by
// the sbt-aspectj plugin.
javaOptions in run <++= AspectjKeys.weaverOptions in Aspectj

// We need to ensure that the JVM is forked for the
// AspectJ Weaver to kick in properly and do it's magic.
fork in run := true

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.thenewmotion.akka" %% "akka-rabbitmq" % "1.2.4",
  "com.typesafe" % "config" % "1.3.0",
  "joda-time" % "joda-time" % "2.8.2",
  "org.json4s" %% "json4s-native" % "3.3.0",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
//  "io.kamon" %% "kamon-core" % "0.5.2",
//  "io.kamon" %% "kamon-akka" % "0.5.2",
//  "io.kamon" %% "kamon-statsd" % "0.5.2",
//  "io.kamon" %% "kamon-log-reporter" % "0.5.2",
//  "io.kamon" %% "kamon-system-metrics" % "0.5.2",
//  "org.aspectj" % "aspectjweaver" % "1.8.5",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.11" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test")

mainClass in Compile := Some("com.demo.ApplicationMain")
