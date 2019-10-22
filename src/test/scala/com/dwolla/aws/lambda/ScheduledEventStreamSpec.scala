package com.dwolla.aws.lambda

import aws.lambda.ScheduledEvent
import com.dwolla.aws.lambda.ScheduledEventStream.terminatingEc2InstanceId
import com.dwolla.testutils.StreamSpec

import scala.scalajs.js
import scala.scalajs.js.Dictionary

class ScheduledEventStreamSpec extends StreamSpec {

  behavior of "ScheduledEventStream"

  it should "accept a ScheduledEvent and return its EC2 Instance ID" inStream {
    val event = js.Dynamic.literal("detail" -> Dictionary("EC2InstanceId" -> "i-abcdefg")).asInstanceOf[ScheduledEvent]

    for {
      instanceId <- terminatingEc2InstanceId(event)
    } yield instanceId should be("i-abcdefg")
  }

}
