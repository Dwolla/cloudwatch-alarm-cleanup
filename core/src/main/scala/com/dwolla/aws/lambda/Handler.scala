package com.dwolla.aws.lambda

import aws.cloudwatch._
import aws.lambda._
import cats.effect._
import com.dwolla.aws.cloudwatch.AlarmsForInstance.byInstanceId
import com.dwolla.aws.cloudwatch._
import com.dwolla.aws.ec2.InstanceId
import com.dwolla.aws.lambda.ScheduledEventStream._
import com.dwolla.lambda.IOLambda
import fs2._
import jsdep.awsLambda.handlerMod

import scala.scalajs.js.annotation._
import scala.scalajs.js.{JSON, _}

object Handler extends IOLambda[ScheduledEvent, Unit] {
  implicit val ioContextShift = IO.contextShift(org.scalajs.macrotaskexecutor.MacrotaskExecutor)

  override def handleRequest(event: ScheduledEvent, context: handlerMod.Context): IO[Unit] =
    Stream.emit(event)
      .covary[IO]
      .through(HandleScheduledEvents[IO])
      .compile
      .drain

  @JSExportTopLevel("removeCloudWatchAlarms")
  val export = handler
}

object HandleScheduledEvents {
  def apply[F[_] : ConcurrentEffect]: Pipe[F, ScheduledEvent, DeleteAlarmsOutput] = input =>
    for {
      ec2Instance <- input.through(scheduledEventToEc2InstanceId)
      implicit0(alg: CloudWatchAlg[F]) <- Stream.resource(CloudWatchAlg.resource[F])
      deletion <- alarmsForInstanceId(ec2Instance).evalTap(alg.clearAlarm).through(alg.removeAlarms).observe(stdOut("deletion result: "))
    } yield deletion

  private def scheduledEventToEc2InstanceId[F[_] : Concurrent]: Pipe[F, ScheduledEvent, InstanceId] = input =>
    for {
      event <- input.observe(stdOut("input: "))
      ec2Instance <- terminatingEc2InstanceId(event).covary[F].observe(stdOut("removing CloudWatch alarms for terminating instance "))
    } yield ec2Instance

  private def alarmsForInstanceId[F[_] : Concurrent : CloudWatchAlg](ec2Instance: InstanceId) =
    CloudWatchAlg[F]
      .listAllCloudWatchAlarms()
      .filter(byInstanceId(ec2Instance))
      .observe(stdOut("alarm "))

  private def stdOut[F[_] : Sync, I](prefix: String)(implicit ev: I => Any): Pipe[F, I, Unit] =
    _.map(JSON.stringify(_)).through(_.evalMap(str => Sync[F].delay(Console.out.println(prefix + str))))

}
