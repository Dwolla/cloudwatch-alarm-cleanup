package com.dwolla.aws.cloudwatch

import aws.AWS.CloudWatch
import aws._
import aws.cloudwatch._
import cats.effect._
import com.dwolla.aws.cloudwatch.CloudWatchAlg.CloudWatchAlgImpl
import com.dwolla.aws.cloudwatch.RemoveAlarmsSpec._
import com.dwolla.aws.cloudwatch.TestHelpers._
import com.dwolla.testutils.StreamSpec
import fs2._
import org.scalatest.matchers.should.Matchers

import scala.scalajs.js

class RemoveAlarmsSpec extends StreamSpec with Matchers {

  behavior of "RemoveAlarms"

  it should "set up a mock client" inIO {
    val cutoff = 100

    val client: CloudWatch = new FakeCloudWatch

    val input: Stream[IO, MetricAlarm] = Stream.emits(0 until (cutoff + 1)).map { i =>
      val metricAlarm = uninitializedMetricAlarm()
      metricAlarm.AlarmName = toAlarmName(i)
      metricAlarm
    }

    val output: IO[List[DeleteAlarmsOutput]] = new CloudWatchAlgImpl[IO](client).removeAlarms(input).compile.toList

    output.map { l =>
      alarms(l.headOption) should be((0 until cutoff).map(toAlarmName).toList)
      alarms(l.tail.headOption) should equal((cutoff until (cutoff + 1)).map(toAlarmName).toList)
    }
  }

  def alarms(output: Option[DeleteAlarmsOutput]): Seq[AlarmName] = output
    .toSeq
    .flatMap {
      _.asInstanceOf[js.Dynamic]
        .selectDynamic("input")
        .asInstanceOf[DeleteAlarmsInput]
        .AlarmNames
        .toSeq
    }

}

object RemoveAlarmsSpec {
  val toAlarmName: Int => AlarmName = i => alarmName(s"alarm-$i")
}

class FakeCloudWatch extends CloudWatch {
  def metricAlarm(x: AlarmName): MetricAlarm = {
    val metricAlarm = uninitializedMetricAlarm()
    metricAlarm.AlarmName = x
    metricAlarm
  }

  override def describeAlarms(params: DescribeAlarmsInput, callback: Callback[DescribeAlarmsOutput]): Unit =
    callback(null, js.Dynamic.literal("MetricAlarms" -> params.AlarmNames.map(metricAlarm)).asInstanceOf[DescribeAlarmsOutput])

  override def deleteAlarms(params: DeleteAlarmsInput, callback: Callback[DeleteAlarmsOutput]): Unit =
    callback(null, js.Dynamic.literal("input" -> params).asInstanceOf[DeleteAlarmsOutput])
}
