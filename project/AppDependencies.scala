import play.core.PlayVersion
import sbt._

object AppDependencies {
  val bootstrapVersion = "8.4.0"
  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-30" % bootstrapVersion
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-30" % bootstrapVersion  ,
    "org.scalatest"          %% "scalatest"              % "3.2.17"            ,
    "org.playframework"      %% "play-test"              % PlayVersion.current ,
    "com.vladsch.flexmark"    % "flexmark-all"           % "0.64.8"          ,
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.1"
  ).map(_ % Test)

  val all: Seq[ModuleID] = compile ++ test
}
