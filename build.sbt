inThisBuild(List(
  organization := "com.dwolla",
  description := "AWS Lambda function that listens for AutoScaling lifecycle events and removes CloudWatch alarms for instances that are terminated",
  homepage := Option(url("https://github.com/Dwolla/cloudwatch-alert-cleanup")),
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  developers := List(
    Developer(
      "bpholt",
      "Brian Holt",
      "bholt+github@dwolla.com",
      url("https://dwolla.com")
    ),
  ),
  scalaVersion := "2.13.8",
  startYear := Option(2018),
  scalaJSLinkerConfig ~= { _.withESFeatures(_.withUseECMAScript2015(false)) },

  githubWorkflowJavaVersions := Seq("adopt@1.8", "adopt@1.11"),
  githubWorkflowTargetTags ++= Seq("v*"),
  githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("test", "package"))),
  githubWorkflowPublishTargetBranches := Nil,
  githubWorkflowPublish := Nil,
))

lazy val V = new {
  val shapeless = "2.3.7"
}

lazy val `aws-types` = project.in(file("aws-types"))
  .enablePlugins(JSDependenciesPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    libraryDependencies ++= Seq(
      "com.chuusai" %%% "shapeless" % V.shapeless,
    ),
    scalacOptions ~= { _.filterNot(Set(
      "-Wdead-code",
      "-Wunused:implicits",
      "-Wunused:explicits",
      "-Wunused:imports",
      "-Wunused:locals",
      "-Wunused:params",
      "-Wunused:patvars",
      "-Wunused:privates",
      "-Wvalue-discard",
    ).contains) },
    (Compile / npmDependencies) ++= Seq(
      "aws-xray-sdk-core" -> "1.2.0",
    ),
  )

lazy val `cloudwatch-alarm-cleanup` = project.in(file("core"))
  .enablePlugins(JSDependenciesPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .settings(
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    libraryDependencies ++= {
      Seq(
        "co.fs2" %%% "fs2-core" % "2.5.11",
        "com.chuusai" %%% "shapeless" % V.shapeless,
        "com.dwolla" %%% "fs2-aws-lambda-io-app" % "2.0.0-M16",
      ) ++
      Seq(
        "org.scalatest" %%% "scalatest" % "3.2.11",
        "com.dwolla" %%% "testutils-scalatest-fs2" % "2.0.0-M6",
      ).map(_ % Test)
    },
    (Compile / npmDevDependencies) ++= Seq(
      "serverless" -> "^1.26.1",
      "serverless-plugin-tracing" -> "^2.0.0",
    ),
    jsDependencies ++= Seq(
      "org.webjars.npm" % "aws-sdk" % "2.1109.0" / "aws-sdk.js" minified "aws-sdk.min.js" commonJSName "AWS",
    ).map(_ % Test),
    webpackConfigFile := Some(baseDirectory.value / "webpack-config.js"),
    webpackResources := webpackResources.value +++ PathFinder(baseDirectory.value / "serverless.yml"),
    artifactPath := target.value / "awslambda.zip",
    applicationBundles := (Compile / fullOptJS / webpack).value
      .filter(_.metadata.get(BundlerFileTypeAttr).exists(_ == BundlerFileType.ApplicationBundle))
      .map(_.data),
    Compile / Keys.`package` := {
      val zipFile = artifactPath.value
      val inputs = applicationBundles.value.map(f => f -> f.name)

      IO.zip(inputs, zipFile, None)

      zipFile
    }
  )
  .dependsOn(`aws-types`)

lazy val `cloudwatch-alert-cleanup-root` = project.in(file("."))
  .settings(publish / skip := true)
  .aggregate(`cloudwatch-alarm-cleanup`, `aws-types`)

lazy val serverlessDeployCommand = settingKey[String]("serverless command to deploy the application")
serverlessDeployCommand := "serverless deploy --verbose"

lazy val deploy = taskKey[Int]("deploy to AWS")
deploy := Def.task {
  val _ = (`cloudwatch-alarm-cleanup` / Compile / Keys.`package`).value

  import scala.sys.process._

  val cmd = serverlessDeployCommand.value
  val webpackWorkingFolder = (`cloudwatch-alarm-cleanup` / Compile / npmUpdate / crossTarget).value
  val nodeModulesBin = webpackWorkingFolder / "node_modules" / ".bin"
  val bundleName = (`cloudwatch-alarm-cleanup` / applicationBundles).value.map(_.name.split('.').head).head

  Process(
    s"$nodeModulesBin/$cmd",
    Option((`cloudwatch-alert-cleanup-root` / baseDirectory).value),
    "SERVICE_NAME" -> (`cloudwatch-alarm-cleanup` / normalizedName).value,
    "ARTIFACT_PATH" -> (`cloudwatch-alarm-cleanup` / artifactPath).value.toString,
    "BUNDLE_NAME" -> bundleName,
  ).!
}.value

lazy val applicationBundles = taskKey[Seq[File]]("webpack bundled application library")
