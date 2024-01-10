name := "coinpayments"

organization := "com.alexdupre"

version := "2.0.0"

scalaVersion := "2.13.12"

crossScalaVersions := Seq(scalaVersion.value, "3.3.1")

scalacOptions := List("-feature", "-unchecked", "-deprecation", "-explaintypes", "-encoding", "UTF8")

resolvers += Resolver.typesafeRepo("releases")

libraryDependencies ++= List(
  "com.eed3si9n" %% "gigahorse-okhttp" % "0.7.0",
  "org.playframework" %% "play-json" % "3.0.1",
  "commons-codec" % "commons-codec" % "1.16.0",
  "org.slf4j" % "slf4j-api" % "2.0.11",
  "ch.qos.logback" % "logback-classic" % "1.4.14" % Test
)

publishTo := sonatypePublishToBundle.value

publishMavenStyle := true

licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php"))

sonatypeProjectHosting := Some(xerial.sbt.Sonatype.GitHubHosting("alexdupre", "coinpayments-scala", "Alex Dupre", "ale@FreeBSD.org"))

buildInfoKeys := Seq[BuildInfoKey](version)

buildInfoPackage := s"${organization.value}.${name.value}"

enablePlugins(BuildInfoPlugin)
