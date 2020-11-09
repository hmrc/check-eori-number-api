import sbt._
import play.core.PlayVersion._

object AppDependencies {

  private val customsApiCommonVersion = "1.53.0"
  private val mockitoVersion = "3.5.9"
  private val scalaTestPlusPlayVersion = "4.0.3"
  private val wireMockVersion = "2.27.2"
  private val scalaTestVersion = "3.1.2"
  private val flexmarkVersion = "0.35.10"
  private val testScope = "test,component"

  val customsApiCommon = "uk.gov.hmrc" %% "customs-api-common" % customsApiCommonVersion withSources()

  val mockito = "org.mockito" % "mockito-core" % mockitoVersion % testScope

  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % testScope

  val playTest = "com.typesafe.play" %% "play-test" % current % testScope

  val scalaTestPlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % testScope

  val wireMock = "com.github.tomakehurst" % "wiremock-jre8" % wireMockVersion % testScope

  val flexmark = "com.vladsch.flexmark" % "flexmark-all" % flexmarkVersion % testScope

  val customsApiCommonTests = "uk.gov.hmrc" %% "customs-api-common" % customsApiCommonVersion % testScope classifier "tests"

}
