import sbt.Keys.{parallelExecution, _}
import sbt._
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, scalaSettings}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

val name = "check-eori-number-api"

PlayKeys.playDefaultPort := 8353

val scoverageExcludePatterns = List("<empty>",
  "Reverse.*",
  "uk.gov.hmrc.*",
  "prod.*",
  "app.Routes.*",
  "config",
  "testOnlyDoNotUseInAppConf",
  "repositories.MongoErrorLogger",
  "controllers.AgentDelegateAuthorisationController")

lazy val scoverageSettings = Seq(
  ScoverageKeys.coverageExcludedPackages := scoverageExcludePatterns.mkString("", ";", ""),
  ScoverageKeys.coverageMinimumStmtTotal := 80,
  ScoverageKeys.coverageFailOnMinimum := true,
  ScoverageKeys.coverageHighlighting := true
)

lazy val plugins: Seq[Plugins] = Seq(PlayScala, SbtDistributablesPlugin)
lazy val playSettings: Seq[Setting[_]] = Seq.empty

lazy val playPublishingSettings: Seq[sbt.Setting[_]] = Seq(
  Compile / packageDoc / publishArtifact := false,
  Compile / packageSrc / publishArtifact := false
)

lazy val microservice = Project(name, file("."))
  .enablePlugins(plugins: _*)
  .settings(
    playSettings,
    majorVersion := 0,
    scalaSettings,
    scalaVersion := "2.13.8",
    scoverageSettings,
    publishingSettings,
    defaultSettings(),
    PlayKeys.playDefaultPort := 9002,
    libraryDependencies ++= AppDependencies.all,
    Test / parallelExecution := false,
    Test / fork := false,
    retrieveManaged := true
  ).settings(
  scalacOptions ++= Seq(
    "-language:postfixOps"
  )
)

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"

Compile / unmanagedResourceDirectories += baseDirectory.value / "public"

