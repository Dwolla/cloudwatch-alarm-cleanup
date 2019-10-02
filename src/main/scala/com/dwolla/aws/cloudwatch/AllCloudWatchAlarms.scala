package com.dwolla.aws.cloudwatch

import aws.AWS.CloudWatch
import aws.cloudwatch._
import cats.effect._
import com.dwolla.awssdk.PaginatedAwsClient._
import fs2._

object AllCloudWatchAlarms {
  def apply[F[_] : Effect](client: CloudWatch): Stream[F, MetricAlarm] =
    (() => DescribeAlarmsInput())
      .fetchAll[F](client.describeAlarms)(_.MetricAlarms.toSeq)
}
