package com.dwolla.aws.lambda

import aws.AWS
import aws.AWSXRay._
import aws.cloudwatch._
import aws.lambda._
import cats.effect._
import com.dwolla.aws.cloudwatch.AlarmsForInstance.byInstanceId
import com.dwolla.aws.cloudwatch._
import com.dwolla.aws.lambda.ScheduledEventStream._
import fs2._
import RemoveAlarms._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.{higherKinds, postfixOps}
import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.scalajs.js.{JSON, _}

object Handler {

  @JSExportTopLevel("removeCloudWatchAlarms")
  val handler: ScheduledHandler = (event, _, callback: LambdaCallback[Unit]) ⇒ {
    Stream.emit(event)
      .covary[IO]
      .through(handleScheduledEvents[IO])
      .compile
      .drain
      .unsafeRunAsync {
        case Left(t) ⇒ callback(js.Error(t.getMessage), js.undefined)
        case Right(()) ⇒ callback(null, ())
      }
  }

  def handleScheduledEvents[F[_]](input: Stream[F, ScheduledEvent])
                                 (implicit F: Effect[F]): Stream[F, DeleteAlarmsOutput] = {
    val aws = captureAWSClient(new AWS.CloudWatch())
    val cloudWatchClientStream = Stream.bracket(F.delay(aws))(Stream.emit(_), _ ⇒ F.unit)

    val stream = for {
      client ← cloudWatchClientStream.observe(stdOut("client: "))
      event ← input.observe(stdOut("input: "))
      ec2Instance ← terminatingEc2InstanceId(event).observe(stdOut("removing CloudWatch alarms for terminating instance "))
      alarms = AllCloudWatchAlarms[F](client)
        .observe(stdOut("alarm "))
        .filter(byInstanceId(ec2Instance))
      deletion ← alarms.through(RemoveAlarms[F](client))
    } yield deletion

    stream
      .observe(stdOut("observe "))
  }

  def stdOut[F[_], I](prefix: String = "")(implicit F: Sync[F]): Sink[F, I] =
    _.map(i ⇒ JSON.stringify(i.asInstanceOf[Any])).to(_.evalMap(str => F.delay(Console.out.println(prefix + str))))

}
