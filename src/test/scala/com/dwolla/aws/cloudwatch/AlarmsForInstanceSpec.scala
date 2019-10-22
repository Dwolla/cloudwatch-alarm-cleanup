package com.dwolla.aws.cloudwatch

import aws.cloudwatch.Dimension
import com.dwolla.aws.cloudwatch.TestHelpers._
import org.scalatest._

import scala.scalajs.js.JSConverters._

class AlarmsForInstanceSpec extends FlatSpec with Matchers {

  val instanceId = TestHelpers.instanceId("i-abcdefg")

  behavior of "AlarmsForInstance"

  it should "return true if the metric's dimensions include one that matches the instance ID" in {
    val metricAlarm = uninitializedMetricAlarm()
    metricAlarm.Dimensions = List("InstanceId" -> instanceId).map(tupleToDimension).toJSArray

    AlarmsForInstance.byInstanceId(instanceId)(metricAlarm) should be(true)
  }

  it should "return false if the metric's dimensions include an InstanceId dimension but it doesn't match the instance ID" in {
    val metricAlarm = uninitializedMetricAlarm()
    metricAlarm.Dimensions = List("InstanceId" -> "i-1234567").map(tupleToDimension).toJSArray

    AlarmsForInstance.byInstanceId(instanceId)(metricAlarm) should be(false)
  }

  it should "return false if the metric's dimensions do not include an InstanceId dimension" in {
    val metricAlarm = uninitializedMetricAlarm()
    metricAlarm.Dimensions = List.empty[Dimension].toJSArray

    AlarmsForInstance.byInstanceId(instanceId)(metricAlarm) should be(false)
  }
}
