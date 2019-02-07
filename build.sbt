name := "coinpayments"

organization := "com.alexdupre"

version := "1.1"

scalaVersion := "2.12.8"

scalacOptions := List("-feature", "-unchecked", "-deprecation", "-explaintypes", "-encoding", "UTF8")

resolvers += Resolver.typesafeRepo("releases")

libraryDependencies ++= List(
  "com.eed3si9n" %% "gigahorse-okhttp" % "0.3.1",
  "com.typesafe.play" %% "play-json" % "2.7.1",
  "commons-codec" % "commons-codec" % "1.11",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "test"
)

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ =>
  false
}

pomExtra := (<url>https://github.com/alexdupre/coinpayments-scala</url>
  <licenses>
    <license>
      <name>BSD-style</name>
      <url>http://www.opensource.org/licenses/bsd-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:alexdupre/coinpayments-scala.git</url>
    <connection>scm:git:git@github.com:alexdupre/coinpayments-scala.git</connection>
  </scm>
  <developers>
    <developer>
      <id>alexdupre</id>
      <name>Alex Dupre</name>
      <url>http://www.alexdupre.com</url>
    </developer>
  </developers>)

buildInfoKeys := Seq[BuildInfoKey](version)

buildInfoPackage := s"${organization.value}.${name.value}"

enablePlugins(BuildInfoPlugin)
