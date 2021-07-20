import sbt.Keys._
import sbt.{Resolver, _}
import uk.gov.hmrc.DefaultBuildSettings.targetJvm
import uk.gov.hmrc.PublishingSettings._
import uk.gov.hmrc.gitstamp.GitStampPlugin._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._

name:= "check-eori-number-api"

PlayKeys.playDefaultPort := 8353

targetJvm := "jvm-1.8"

lazy val microservice = (project in file("."))
  .enablePlugins(PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin)
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
  .settings(
    majorVersion := 0,
    scalaVersion := "2.12.13",
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    commonSettings,
    playPublishingSettings,
    scoverageSettings
  )
  .settings(resolvers += Resolver.jcenterRepo)

lazy val commonSettings: Seq[Setting[_]] = publishingSettings ++ gitStampSettings

lazy val playPublishingSettings: Seq[Setting[_]] = Seq(credentials += SbtCredentials) ++
  Seq(credentials += SbtCredentials) ++
  publishAllArtefacts

lazy val scoverageSettings: Seq[Setting[_]] = Seq(
  coverageExcludedPackages := "<empty>;.*(Reverse|Routes).*;com.kenshoo.play.metrics.*;.*definition.*;prod.*;testOnlyDoNotUseInAppConf.*;app.*;uk.gov.hmrc.BuildInfo;views.*;uk.gov.hmrc.apinotificationpull.config.*",
  coverageMinimumStmtTotal := 96,
  coverageFailOnMinimum := true,
  coverageHighlighting := true,
  parallelExecution in Test := false
)

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"

unmanagedResourceDirectories in Compile += baseDirectory.value / "public"

