import sbt._
import play.core.PlayVersion._

object AppDependencies {

  private val bootstrapBackendPlayVersion = "5.3.0"
  private val bootstrapTestPlayVersion = "4.3.0"
  private val scalaTestPlusPlayVersion = "4.0.3"
  private val scalaTestVersion = "3.1.2"
  private val flexmarkVersion = "0.35.10"
  private val testScope = "test,component"

  val bootstrapBackendPlay = "uk.gov.hmrc" %% "bootstrap-backend-play-27" % bootstrapBackendPlayVersion withSources()
  val bootstrapTestPlay = "uk.gov.hmrc" %% "bootstrap-test-play-27"   % bootstrapTestPlayVersion % Test
  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % testScope
  val playTest = "com.typesafe.play" %% "play-test" % current % testScope
  val scalaTestPlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % testScope
  val flexmark = "com.vladsch.flexmark" % "flexmark-all" % flexmarkVersion % testScope

}
