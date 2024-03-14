import sbt.Keys.{parallelExecution, *}
import sbt.*
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings
import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, scalaSettings}

scalafmtOnCompile := true
ThisBuild / majorVersion := 1
ThisBuild / scalaVersion := "2.13.13"

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageExcludedPackages := scoverageExcludePatterns.mkString("", ";", ""),
  ScoverageKeys.coverageMinimumStmtTotal := 80,
  ScoverageKeys.coverageFailOnMinimum    := true,
  ScoverageKeys.coverageHighlighting     := true
)

PlayKeys.playDefaultPort := 8353

scalacOptions += "-Wconf:src=html/.*:s"
scalacOptions += "-Wconf:src=routes/.*:s"
scalacOptions += "-Wconf:src=txt/.*:s"
lazy val plugins: Seq[Plugins]         = Seq(PlayScala, SbtDistributablesPlugin)
lazy val playSettings: Seq[Setting[_]] = Seq.empty
lazy val playPublishingSettings: Seq[sbt.Setting[_]] = Seq(
  Compile / packageDoc / publishArtifact := false,
  Compile / packageSrc / publishArtifact := false
)
lazy val microservice = Project(name, file("."))
  .enablePlugins(plugins: _*)
  .settings(
    playSettings,
    scalaSettings,
    scoverageSettings,
    defaultSettings(),
    PlayKeys.playDefaultPort := 9002,
    libraryDependencies ++= AppDependencies.all,
    Test / parallelExecution := false,
    Test / fork              := false,
    retrieveManaged          := true
  ).settings(
    scalacOptions ++= Seq(
      "-language:postfixOps"
    )
  )

val name = "check-eori-number-api"
val scoverageExcludePatterns = List(
  "<empty>",
  "Reverse.*",
  "prod.*",
  "app.Routes.*",
  ".*Routes.*",
  "config",
  "uk.gov.hmrc.checkeorinumberapi.controllers.ApiDocumentationController",
  ".*views.*"
)

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test") // the "test->test" allows reusing test code and test dependencies
  .settings(DefaultBuildSettings.itSettings())
  .settings(libraryDependencies ++= AppDependencies.test)

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"

Compile / unmanagedResourceDirectories += baseDirectory.value / "public"
