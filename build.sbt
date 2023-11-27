import sbt.Keys.{parallelExecution, _}
import sbt._
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, scalaSettings}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings

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
    majorVersion := 1,
    scalaSettings,
    scalaVersion := "2.13.8",
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
  "uk.gov.hmrc.*",
  "prod.*",
  "app.Routes.*",
  "config",
  "testOnlyDoNotUseInAppConf",
  "repositories.MongoErrorLogger",
  "controllers.AgentDelegateAuthorisationController"
)

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"

Compile / unmanagedResourceDirectories += baseDirectory.value / "public"
