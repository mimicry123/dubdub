name := "user"

version := "0.1"

scalaVersion := "2.12.4"


libraryDependencies +="com.twitter" %% "finatra-thrift" % "17.12.0"

libraryDependencies += "com.twitter" %% "util-logging" % "17.12.0"

// https://mvnrepository.com/artifact/com.twitter/finagle-redis
libraryDependencies += "com.twitter" %% "finagle-redis" % "17.12.0"
