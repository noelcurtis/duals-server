import play.PlayScala

name := """clash"""

version := "1.0.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.datastax.cassandra" % "cassandra-driver-core" % "2.1.3",
  "org.scaldi" %% "scaldi-play" % "0.4.1"
)
