package com.dwolla.aws.cloudwatch

import aws.AWS.CloudWatch
import aws.cloudwatch._
import cats.effect._
import com.dwolla.awssdk.PaginatedAwsClient._
import fs2._

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

object AllCloudWatchAlarms {
  def apply[F[_] : Effect](client: CloudWatch)(implicit ec: ExecutionContext): Stream[F, MetricAlarm] =
    (() ⇒ DescribeAlarmsInput())
      .fetchAll[F](client.describeAlarms)(_.MetricAlarms)
}
