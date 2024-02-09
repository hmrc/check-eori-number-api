resolvers += "HMRC-open-artefacts-maven" at "https://open.artefacts.tax.service.gov.uk/maven2"
resolvers += Resolver.url("HMRC-open-artefacts-ivy", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)
resolvers += Resolver.jcenterRepo

// To resolve a bug with version 2.x.x of the scoverage plugin - https://github.com/sbt/sbt/issues/6997
ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)

addSbtPlugin("uk.gov.hmrc"       % "sbt-distributables"    % "2.5.0")
addSbtPlugin("com.typesafe.play" % "sbt-plugin"            % "2.9.0")
addSbtPlugin("net.virtual-void"  % "sbt-dependency-graph"  % "0.10.0-RC1")
addSbtPlugin("org.scoverage"     % "sbt-scoverage"         % "1.9.3")
addSbtPlugin("org.scalastyle"   %% "scalastyle-sbt-plugin" % "1.0.0")
addSbtPlugin("uk.gov.hmrc"       % "sbt-auto-build"        % "3.20.0")
addSbtPlugin("org.scalameta"     % "sbt-scalafmt"          % "2.5.0")
