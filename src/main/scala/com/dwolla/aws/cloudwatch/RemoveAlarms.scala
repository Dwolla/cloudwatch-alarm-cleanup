package com.dwolla.aws.cloudwatch

import aws.AWS.CloudWatch
import aws.cloudwatch._
import cats.effect.Effect
import com.dwolla.awssdk.ExecuteVia._
import fs2._

import scala.concurrent.ExecutionContext
import scala.language.{higherKinds, implicitConversions}

object RemoveAlarms {
  val deleteAlarmsParallelismFactor = 5
  val maximumNumberOfAlarmsToDeleteAtOnce = 100

  def apply[F[_] : Effect](deleteAlarms: DeleteAlarms[F])
                          (alarms: Stream[F, MetricAlarm])
                          (implicit ec: ExecutionContext): Stream[F, DeleteAlarmsOutput] = {
    alarms
      .map(_.AlarmName)
      .segmentN(maximumNumberOfAlarmsToDeleteAtOnce, allowFewer = true)
      .map(segment ⇒ segment.force.toList)
      .map(segment ⇒ DeleteAlarmsInput(segment: _*))
      .map(deleteAlarms)
      .map(Stream.eval)
      .join(deleteAlarmsParallelismFactor)
  }

  type DeleteAlarms[F[_]] = DeleteAlarmsInput ⇒ F[DeleteAlarmsOutput]

  implicit def toDeleteAlarms[F[_] : Effect](client: CloudWatch): DeleteAlarms[F] = _.executeVia[F](client.deleteAlarms)
}
