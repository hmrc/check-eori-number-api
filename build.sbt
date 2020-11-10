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
  Resolver.bintrayRepo("hmrc", "releases"),
  Resolver.jcenterRepo
)

lazy val CdsComponentTest = config("component") extend Test

val testConfig = Seq(CdsComponentTest, Test)

def forkedJvmPerTestConfig(tests: Seq[TestDefinition], packages: String*): Seq[Group] =
  tests.groupBy(_.name.takeWhile(_ != '.')).filter(packageAndTests => packages contains packageAndTests._1) map {
    case (packg, theTests) =>
      Group(packg, theTests, SubProcess(ForkOptions()))
  } toSeq

lazy val testAll = TaskKey[Unit]("test-all")
lazy val allTest = Seq(testAll := (test in CdsComponentTest).dependsOn(test in Test).value)

lazy val microservice = (project in file("."))
  .enablePlugins(PlayScala)
  .enablePlugins(SbtAutoBuildPlugin, SbtGitVersioning)
  .enablePlugins(SbtDistributablesPlugin)
  .enablePlugins(SbtArtifactory)
  .disablePlugins(sbt.plugins.JUnitXmlReportPlugin)
  .configs(testConfig: _*)
  .settings(
    commonSettings,
    unitTestSettings,
    componentTestSettings,
    playPublishingSettings,
    allTest,
    scoverageSettings,
    allResolvers
  )
  .settings(majorVersion := 0)

lazy val unitTestSettings =
  inConfig(Test)(Defaults.testTasks) ++
    Seq(
      testOptions in Test := Seq(Tests.Filter(unitTestFilter)),
      unmanagedSourceDirectories in Test := Seq((baseDirectory in Test).value / "test"),
      addTestReportOption(Test, "test-reports")
    )

lazy val componentTestSettings =
  inConfig(CdsComponentTest)(Defaults.testTasks) ++
    Seq(
      testOptions in CdsComponentTest := Seq(Tests.Filter(componentTestFilter)),
      fork in CdsComponentTest := false,
      parallelExecution in CdsComponentTest := false,
      addTestReportOption(CdsComponentTest, "comp-test-reports"),
      testGrouping in CdsComponentTest := forkedJvmPerTestConfig((definedTests in Test).value, "component")
    )

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

def componentTestFilter(name: String): Boolean = name startsWith "component"
def unitTestFilter(name: String): Boolean = !componentTestFilter(name)

scalastyleConfig := baseDirectory.value / "project" / "scalastyle-config.xml"

val compileDependencies = Seq(customsApiCommon)

val testDependencies = Seq(scalaTestPlusPlay, customsApiCommonTests, playTest, scalaTest, flexmark)

unmanagedResourceDirectories in Compile += baseDirectory.value / "public"

libraryDependencies ++= compileDependencies ++ testDependencies
