lazy val `cloudwatch-alarm-cleanup` = project.in(file("."))
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    resolvers ++= Seq(
      Resolver.jcenterRepo,
      Resolver.sonatypeRepo("releases"),
      Resolver.bintrayRepo("dwolla", "maven"),
    ),
    libraryDependencies ++= {
      Seq(
        "co.fs2" %%% "fs2-core" % "2.0.1",
        "com.chuusai" %%% "shapeless" % "2.3.3",
      ) ++
      Seq(
        "org.scalatest" %%% "scalatest" % "3.0.8",
        "com.dwolla" %%% "testutils-scalatest-fs2" % "2.0.0-M3",
      ).map(_ % Test)
    },
    (npmDependencies in Compile) ++= Seq(
      "aws-xray-sdk-core" -> "1.2.0",
    ),
    (npmDevDependencies in Compile) ++= Seq(
      "serverless" -> "^1.26.1",
      "serverless-plugin-tracing" -> "^2.0.0",
    ),
    jsDependencies ++= Seq(
      "org.webjars.npm" % "aws-sdk" % "2.200.0" / "aws-sdk.js" minified "aws-sdk.min.js" commonJSName "AWS",
    ).map(_ % Test),
    scalaVersion := "2.13.4",
    webpackConfigFile := Some(baseDirectory.value / "webpack-config.js"),
    webpackResources := webpackResources.value +++ PathFinder(baseDirectory.value / "serverless.yml"),
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
    scalacOptions --= Seq(
      "-Wdead-code",
      "-Wunused:explicits",
      "-Wunused:params"
    )
  )

lazy val serverlessDeployCommand = settingKey[String]("serverless command to deploy the application")
serverlessDeployCommand := "serverless deploy --verbose"

lazy val deploy = taskKey[Int]("deploy to AWS")
deploy := Def.task {
  val _ = (Compile / Keys.`package`).value

  import scala.sys.process._

  val cmd = serverlessDeployCommand.value
  val webpackWorkingFolder = (Compile / npmUpdate / crossTarget).value
  val nodeModulesBin = webpackWorkingFolder / "node_modules" / ".bin"
  val bundleName = applicationBundles.value.map(_.name.split('.').head).head

  Process(
    s"$nodeModulesBin/$cmd",
    Option(webpackWorkingFolder),
    "SERVICE_NAME" -> normalizedName.value,
    "ARTIFACT_PATH" -> artifactPath.value.toString,
    "BUNDLE_NAME" -> bundleName,
  ).!
}.value

artifactPath := target.value / "awslambda.zip"

Keys.`package` in Compile := {
  val zipFile = artifactPath.value
  val inputs = applicationBundles.value.map(f => f -> f.name)

  IO.zip(inputs, zipFile, None)

  zipFile
}

lazy val applicationBundles = taskKey[Seq[File]]("webpack bundled application library")
applicationBundles := (Compile / fullOptJS / webpack).value
  .filter(_.metadata.get(BundlerFileTypeAttr).exists(_ == BundlerFileType.ApplicationBundle))
  .map(_.data)
