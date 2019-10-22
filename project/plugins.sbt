addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.0.6")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.8")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.0")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.12")

// TODO: Use H2 database instead.
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.8"
addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.3.5")
