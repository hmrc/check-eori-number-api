import play.core.PlayVersion
import sbt.*

object AppDependencies {
  val bootstrapVersion = "10.8.0"
  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"         %% "bootstrap-backend-play-30" % bootstrapVersion exclude("org.apache.commons", "commons-lang3"),
    "org.apache.commons"  %  "commons-lang3"             % "3.20.0",
    "org.mozilla"         %  "rhino"                     % "1.9.1",
    "ch.qos.logback"      % "logback-core"               % "1.6.3",
    "ch.qos.logback"      % "logback-classic"            % "1.6.3",
    "at.yawk.lz4"         %  "lz4-java"                  % "1.11.3"
  )

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.scalatest"          %% "scalatest"              % "3.2.20",
    "org.playframework"      %% "play-test"              % PlayVersion.current,
    "com.vladsch.flexmark"    % "flexmark-all"           % "0.64.8",
    "org.scalatestplus.play" %% "scalatestplus-play"     % "7.0.2"
  ).map(_ % Test)

  val all: Seq[ModuleID] = compile ++ test
}
