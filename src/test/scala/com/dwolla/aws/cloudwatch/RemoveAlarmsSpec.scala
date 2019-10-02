package com.dwolla.aws.cloudwatch

import aws.cloudwatch.{AlarmName, DeleteAlarmsInput, DeleteAlarmsOutput, MetricAlarm}
import cats.effect.IO
import com.dwolla.aws.cloudwatch.RemoveAlarms._
import com.dwolla.aws.cloudwatch.RemoveAlarmsSpec._
import com.dwolla.aws.cloudwatch.TestHelpers._
import com.dwolla.testutils.StreamSpec
import fs2._
import org.scalatest._

import scala.scalajs.js

class RemoveAlarmsSpec extends StreamSpec with Matchers {

  behavior of "RemoveAlarms"

  it should "set up a mock client" inIO {
    val cutoff = 100

    val client: DeleteAlarms[IO] = input => IO {
      js.Dynamic.literal("input" -> input).asInstanceOf[DeleteAlarmsOutput]
    }

    val input: Stream[IO, MetricAlarm] = Stream.emits(0 until (cutoff + 1)).map { i =>
      val metricAlarm = uninitializedMetricAlarm()
      metricAlarm.AlarmName = toAlarmName(i)
      metricAlarm
    }

    val compiled = RemoveAlarms[IO](client)(input)
      .compile

    compiled.toList.map { l =>
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
