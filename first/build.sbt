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

// https://mvnrepository.com/artifact/com.twitter/finagle-mysql
libraryDependencies += "com.twitter" %% "finagle-mysql" % "17.12.0"

val jacksonExclusion = Seq(
  ExclusionRule("com.fasterxml.jackson.core"),
  ExclusionRule("com.fasterxml.jackson.databind"),
  ExclusionRule("com.fasterxml.jackson.module")
)

libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-http" % "6.1.2"  excludeAll(jacksonExclusion: _*)

libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-core" % "6.1.2"  excludeAll(jacksonExclusion: _*)

libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-embedded" % "6.1.2"  excludeAll(jacksonExclusion: _*)

// https://mvnrepository.com/artifact/com.twitter/bijection-core
libraryDependencies += "com.twitter" %% "bijection-core" % "0.9.6" excludeAll(jacksonExclusion: _*)

libraryDependencies += "com.twitter" %% "bijection-util" % "0.9.6" excludeAll(jacksonExclusion: _*)




assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}