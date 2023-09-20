val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "ls",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test,
    libraryDependencies += "org.jsoup" % "jsoup" % "1.14.3",
    libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "3.0.0",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.10.0-RC9"
  )
