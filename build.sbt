ThisBuild / scalaVersion := "2.13.1"
ThisBuild / organization := "com.github.nomadblacky"
ThisBuild / organizationName := "NomadBlacky"
ThisBuild / resolvers ++= Seq(
  "digdag" at "https://dl.bintray.com/digdag/maven/",
  "spring-plugins" at "https://repo.spring.io/plugins-release/" // for org.embulk:guice-bootstrap
)

lazy val root = (project in file("."))
  .enablePlugins(ScalikejdbcPlugin)
  .settings(
    name := "digstats",
    libraryDependencies ++= Seq(
        "io.digdag"       % "digdag-cli"          % "0.9.39" % Compile,
        "org.postgresql"  % "postgresql"          % "42.2.8",
        "org.scalikejdbc" %% "scalikejdbc"        % "3.3.5",
        "org.scalatest"   %% "scalatest"          % "3.0.8" % Test,
        "org.scalikejdbc" %% "scalikejdbc-test"   % "3.3.5" % Test,
        "org.scalikejdbc" %% "scalikejdbc-config" % "3.3.5" % Test
      )
    // TODO: Add a task that migrate database by Digdag and generate sources of scalikejdbc.
  )
