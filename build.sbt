import AppDependencies._
import sbt.Keys._
import sbt.Tests.{Group, SubProcess}
import sbt.{Resolver, _}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, integrationTestSettings, targetJvm}
import uk.gov.hmrc.PublishingSettings._
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import uk.gov.hmrc.gitstamp.GitStampPlugin._

PlayKeys.playDefaultPort := 8353

name:= "check-eori-number-api"
scalaVersion := "2.12.11"
targetJvm := "jvm-1.8"

lazy val allResolvers = resolvers ++= Seq(
  Resolver.jcenterRepo
)

lazy val microservice = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
  .enablePlugins(SbtDistributablesPlugin)
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
  .settings(
    commonSettings,
    playPublishingSettings,
    scoverageSettings,
    allResolvers
  )
  .settings(majorVersion := 0)

lazy val commonSettings: Seq[Setting[_]] = publishingSettings ++ gitStampSettings

lazy val playPublishingSettings: Seq[sbt.Setting[_]] = Seq(credentials += SbtCredentials) ++
  Seq(credentials += SbtCredentials) ++
  publishAllArtefacts

lazy val scoverageSettings: Seq[Setting[_]] = Seq(
  coverageExcludedPackages := "<empty>;.*(Reverse|Routes).*;com.kenshoo.play.metrics.*;.*definition.*;prod.*;testOnlyDoNotUseInAppConf.*;app.*;uk.gov.hmrc.BuildInfo;views.*;uk.gov.hmrc.apinotificationpull.config.*",
  coverageMinimum := 96,
  coverageFailOnMinimum := true,
  coverageHighlighting := true,
  parallelExecution in Test := false
)

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"

val compileDependencies = Seq(bootstrapBackendPlay)

val testDependencies = Seq(scalaTestPlusPlay, bootstrapTestPlay, playTest, scalaTest, flexmark)

unmanagedResourceDirectories in Compile += baseDirectory.value / "public"

libraryDependencies ++= compileDependencies ++ testDependencies
