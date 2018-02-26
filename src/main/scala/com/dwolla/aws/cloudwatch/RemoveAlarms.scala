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

  def apply[F[_] : Effect](client: CloudWatch)
                          (alarms: Stream[F, MetricAlarm])
                          (implicit ec: ExecutionContext): Stream[F, DeleteAlarmsOutput] = {
    alarms
      .map(_.AlarmName)
      .segmentN(maximumNumberOfAlarmsToDeleteAtOnce, allowFewer = true)
      .map(segment ⇒ segment.force.toList)
      .map(segment ⇒ DeleteAlarmsInput(segment: _*))
      .map(_.executeVia[F](client.deleteAlarms))
      .map(Stream.eval)
      .join(deleteAlarmsParallelismFactor)
  }

}
