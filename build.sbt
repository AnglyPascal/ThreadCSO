ThisBuild / organization := "ox"
Global / resolvers += "scala-integration" at
  "https://scala-ci.typesafe.com/artifactory/scala-integration/"
ThisBuild / scalaVersion := "2.13.11-bin-114c1da"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / fork := true
ThisBuild / javaOptions ++= Seq("--enable-preview") // when running
ThisBuild / javacOptions ++= Seq("--enable-preview", "--release", "14") // when compiling,

/* ThisBuild / javaOptions ++= Seq("-Dio.threadcso.pool.KIND=ADAPTIVE") */

lazy val scalaReflect = Def.setting {
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
}

lazy val core = (project in file("core"))
  .dependsOn(macroSub)
  .dependsOn(app % "test")
  .settings(
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked"
      /* "-Werror" */
    ),
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
  )

lazy val macroSub = (project in file("macros"))
  .settings(
    libraryDependencies += scalaReflect.value
  )

lazy val app = (project in file("app"))
  .settings(
    libraryDependencies += scalaReflect.value
  )
