name := "root"
organization := "com.michaelpollmeier"
publishArtifact := false

scalaVersion := "2.11.8"

val commonSettings = Seq(
  organization := "com.michaelpollmeier",
  licenses +=("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html")),
  homepage := Some(url("https://github.com/mpollmeier/gremlin-scala")),
  version := "3.2.0-incubating.1-SNAPSHOT",
  scalaVersion := "2.11.8",

  libraryDependencies <++= scalaVersion { scalaVersion =>
    val gremlinVersion = "3.2.0-incubating"
    Seq(
      "org.apache.tinkerpop" % "gremlin-core" % gremlinVersion exclude("org.slf4j", "slf4j-log4j12"),
      "org.scala-lang" % "scala-reflect" % scalaVersion,
      "com.novocode" % "junit-interface" % "0.11" % "test->default",
      "com.chuusai" %% "shapeless" % "2.3.1",
      "org.scala-lang.modules" %% "scala-xml" % "1.0.5", //just specified to eliminate sbt warnings
      "org.apache.tinkerpop" % "tinkergraph-gremlin" % gremlinVersion % Test exclude("org.slf4j", "slf4j-log4j12"),
      "org.apache.tinkerpop" % "gremlin-test" % gremlinVersion % Test,
      "junit" % "junit" % "4.12" % Test,
      "org.scalatest" %% "scalatest" % "2.2.6" % Test
    )
  },
  resolvers += "Apache public" at "https://repository.apache.org/content/groups/public/",
  scalacOptions ++= Seq(
    "-Xlint"
    // "-Xfatal-warnings",
    // "-feature"
    // "-deprecation", //hard to handle when supporting multiple scala versions...
    // , "-Xlog-implicits"
    //"-Ydebug"
  ),
  // testOptions in Test += Tests.Argument("-oF"), // full stack traces
  incOptions := incOptions.value.withNameHashing(true), // doesn't work on travis ;(
  publishTo := {
    val sonatype = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at sonatype + "content/repositories/snapshots")
    else
      Some("releases" at sonatype + "service/local/staging/deploy/maven2")
  },
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra :=
    <scm>
      <url>git@github.com:mpollmeier/gremlin-scala.git</url>
      <connection>scm:git:git@github.com:mpollmeier/gremlin-scala.git</connection>
    </scm>
      <developers>
        <developer>
          <id>mpollmeier</id>
          <name>Michael Pollmeier</name>
          <url>http://www.michaelpollmeier.com</url>
        </developer>
      </developers>
)

lazy val root = project.in(file("."))
  .aggregate(`gremlin-scala`, macros)

lazy val `gremlin-scala` = project.in(file("gremlin-scala"))
  .settings(commonSettings: _*)
  .dependsOn(macros)

// macros can't be in the same compilation unit according to joan: https://github.com/mpollmeier/gremlin-scala/issues/100
lazy val macros = project.in(file("macros"))
  .settings(commonSettings: _*)
