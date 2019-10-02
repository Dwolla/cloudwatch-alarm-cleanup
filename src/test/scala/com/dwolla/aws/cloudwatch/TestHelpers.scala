package com.dwolla.aws.cloudwatch

import aws.cloudwatch.{AlarmName, AlarmNameTag, Dimension, MetricAlarm}
import com.dwolla.aws.ec2._
import shapeless.tag

import scala.scalajs.js

object TestHelpers {
  def uninitializedMetricAlarm(): MetricAlarm = js.Dynamic.literal().asInstanceOf[MetricAlarm]
  def tupleToDimension(tuple: (String, String)): Dimension =
    js.Dynamic.literal("Name" → tuple._1, "Value" → tuple._2).asInstanceOf[Dimension]
  def instanceId(s: String): InstanceId = tag[InstanceIdTag][String](s)
  def alarmName(s: String): AlarmName = tag[AlarmNameTag][String](s)

}
