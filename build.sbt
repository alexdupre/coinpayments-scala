name := "coinpayments"

organization := "com.alexdupre"

version := "1.3"

scalaVersion := "2.13.10"

crossScalaVersions := Seq("2.12.10", scalaVersion.value, "3.2.2")

scalacOptions := List("-feature", "-unchecked", "-deprecation", "-explaintypes", "-encoding", "UTF8")

resolvers += Resolver.typesafeRepo("releases")

libraryDependencies ++= List(
  "com.eed3si9n" %% "gigahorse-okhttp" % "0.7.0",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC7",
  "commons-codec" % "commons-codec" % "1.15",
  "org.slf4j" % "slf4j-api" % "2.0.6",
  "ch.qos.logback" % "logback-classic" % "1.3.5" % "test"
)

publishTo := sonatypePublishToBundle.value

publishMavenStyle := true

licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))

sonatypeProjectHosting := Some(xerial.sbt.Sonatype.GitHubHosting("alexdupre", "coinpayments-scala", "Alex Dupre", "ale@FreeBSD.org"))

buildInfoKeys := Seq[BuildInfoKey](version)

buildInfoPackage := s"${organization.value}.${name.value}"

enablePlugins(BuildInfoPlugin)
