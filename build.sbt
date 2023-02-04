/* scalaVersion := "2.13.6" */

/* name := "ThreadCSO" */
/* organization := "ox" */
/* version := "1.0" */

/* libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.1" */

ThisBuild / organization := "ox"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val scalaReflect = Def.setting { "org.scala-lang" % "scala-reflect" % scalaVersion.value }

lazy val core = (project in file("core"))
  .dependsOn(macroSub)
  .settings(
      scalacOptions ++= Seq("-deprecation", "-unchecked", "-Werror"),
      libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )

lazy val macroSub = (project in file("macros"))
  .settings(
    libraryDependencies += scalaReflect.value
  )

lazy val app = (project in file("app"))
  .settings(
    libraryDependencies += scalaReflect.value
  )
