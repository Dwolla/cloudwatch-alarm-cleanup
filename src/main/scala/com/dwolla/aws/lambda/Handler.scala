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
import cats.Applicative

import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.scalajs.js.{JSON, _}

object Handler {
  implicit val ioContextShift = IO.contextShift(scala.concurrent.ExecutionContext.global)

  @JSExportTopLevel("removeCloudWatchAlarms")
  val handler: ScheduledHandler = (event, _, callback: LambdaCallback[Unit]) => {
    Stream.emit(event)
      .covary[IO]
      .through(handleScheduledEvents[IO])
      .compile
      .drain
      .unsafeRunAsync {
        case Left(t) => callback(js.Error(t.getMessage), ())
        case Right(()) => callback(null, ())
      }
  }

  def handleScheduledEvents[F[_] : ConcurrentEffect](input: Stream[F, ScheduledEvent]): Stream[F, DeleteAlarmsOutput] = {
    val acquireCloudWatchClient = Sync[F].delay(captureAWSClient(new AWS.CloudWatch()))
    val cloudWatchClientStream = Stream.bracket(acquireCloudWatchClient)(_ => Applicative[F].unit)

    val stream = for {
      client <- cloudWatchClientStream.observe(stdOut("client: "))
      event <- input.observe(stdOut("input: "))
      ec2Instance <- terminatingEc2InstanceId(event).covary[F].observe(stdOut("removing CloudWatch alarms for terminating instance "))
      alarms = AllCloudWatchAlarms[F](client)
        .observe(stdOut("alarm "))
        .filter(byInstanceId(ec2Instance))
      deletion <- alarms.through(RemoveAlarms[F](client))
    } yield deletion

    stream
      .observe(stdOut("observe "))
  }

  def stdOut[F[_] : Sync, I](prefix: String = "")(implicit ev: I => Any): Pipe[F, I, Unit] =
    _.map(JSON.stringify(_)).through(_.evalMap(str => Sync[F].delay(Console.out.println(prefix + str))))

}
