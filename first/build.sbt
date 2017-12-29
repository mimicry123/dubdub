name := "first"

version := "0.1"

scalaVersion := "2.12.4"

lazy val versions = new {
  val finatra = "2.1.2"
  val logback = "1.1.3"
}

lazy val core = RootProject(file("../user"))

dependsOn(core)

libraryDependencies += "com.twitter" %% "finatra-http" % "17.12.0"
// https://mvnrepository.com/artifact/com.twitter/util-logging
libraryDependencies += "com.twitter" %% "util-logging" % "17.12.0"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}