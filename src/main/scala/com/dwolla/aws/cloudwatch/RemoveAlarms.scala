package com.dwolla.aws.cloudwatch

import aws.AWS.CloudWatch
import aws.cloudwatch._
import cats.effect._
import com.dwolla.awssdk.ExecuteVia._
import fs2._

object RemoveAlarms {
  val deleteAlarmsParallelismFactor = 5
  val maximumNumberOfAlarmsToDeleteAtOnce = 100

  def apply[F[_] : ConcurrentEffect](deleteAlarms: DeleteAlarms[F])
                                    (alarms: Stream[F, MetricAlarm]): Stream[F, DeleteAlarmsOutput] = {
    alarms
      .map(_.AlarmName)
      .chunkN(maximumNumberOfAlarmsToDeleteAtOnce, allowFewer = true)
      .map(_.toList)
      .map(DeleteAlarmsInput(_: _*))
      .map(deleteAlarms)
      .map(Stream.eval)
      .parJoin(deleteAlarmsParallelismFactor)
  }

  type DeleteAlarms[F[_]] = DeleteAlarmsInput => F[DeleteAlarmsOutput]

  implicit def toDeleteAlarms[F[_] : Effect](client: CloudWatch): DeleteAlarms[F] = _.executeVia[F](client.deleteAlarms)
}
