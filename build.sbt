ThisBuild / organization := "ox"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "0.1.0-SNAPSHOT"

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
