import sbt._
import play.core.PlayVersion._

object AppDependencies {
  val bootstrapVersion = "7.8.0"
  val compile = Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-28" % bootstrapVersion,
    compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.7.5" cross CrossVersion.full),
    "com.github.ghik" % "silencer-lib" % "1.7.5" % Provided cross CrossVersion.full
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-28" % bootstrapVersion % Test,
    "org.scalatest" %% "scalatest" % "3.2.12" % Test,
    "com.typesafe.play" %% "play-test" % current % Test,
    "com.vladsch.flexmark" % "flexmark-all" % "0.62.2" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
  )

}
