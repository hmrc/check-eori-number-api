import sbt._
import play.core.PlayVersion._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "5.7.0"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"   % "5.7.0"                 % Test,
    "org.scalatest"           %% "scalatest"                % "3.2.9"                 % Test,
    "com.typesafe.play"       %% "play-test"                % current                 % Test,
    "com.vladsch.flexmark"    %  "flexmark-all"             % "0.35.10"               % Test,
    "org.scalatestplus.play"  %% "scalatestplus-play"       % "5.0.0"                 % Test
  )

}
