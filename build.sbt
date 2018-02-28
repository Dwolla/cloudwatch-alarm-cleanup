lazy val `cloudwatch-alarm-cleanup` = project.in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    resolvers ++= Seq(
      Resolver.jcenterRepo,
      Resolver.sonatypeRepo("releases"),
    ),
    libraryDependencies ++= {
      Seq(
        "co.fs2" %%% "fs2-core" % "0.10.1",
        "com.chuusai" %%% "shapeless" % "2.3.3",
      )
    },
    scalaJSModuleKind := ModuleKind.CommonJSModule,
    scalacOptions += "-P:scalajs:sjsDefinedByDefault",
  )

lazy val serverlessDeployCommand = settingKey[String]("serverless command to deploy the application")
serverlessDeployCommand := "serverless deploy --verbose"

lazy val deploy = taskKey[Int]("deploy to AWS")
deploy := Def.task {
  (Keys.`package` in Compile).value

  import scala.sys.process._

  Process(serverlessDeployCommand.value, None, "SERVICE_NAME" → normalizedName.value, "ARTIFACT_PATH" → artifactPath.value.toString).!
}.value

artifactPath := target.value / "awslambda.zip"

Keys.`package` in Compile := {
  val zipFile = artifactPath.value
  val inputs = Seq((fullOptJS in Compile).value.data → "index.js")

  IO.zip(inputs, zipFile)

  zipFile
}
