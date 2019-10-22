ThisBuild / scalaVersion := "2.13.1"
ThisBuild / organization := "com.github.nomadblacky"
ThisBuild / organizationName := "NomadBlacky"
ThisBuild / resolvers ++= Seq(
  "digdag" at "https://dl.bintray.com/digdag/maven/",
  "spring-plugins" at "https://repo.spring.io/plugins-release/" // for org.embulk:guice-bootstrap
)

lazy val root = (project in file("."))
  .settings(
    name := "digstats",
    libraryDependencies ++= Seq(
        "io.digdag"     % "digdag-cli" % "0.9.39" % Compile,
        "org.scalatest" %% "scalatest" % "3.0.8"  % Test
      )
  )
