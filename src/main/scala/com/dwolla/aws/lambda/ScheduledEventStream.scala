package com.dwolla.aws.lambda

import aws.lambda.ScheduledEvent
import com.dwolla.aws.ec2._
import fs2._
import com.dwolla.fs2utils._
import shapeless.tag

import scala.language.higherKinds

object ScheduledEventStream {

  private def scheduledEventDetails(input: ScheduledEvent): Stream[Pure, Map[String, AnyRef]] =
    Stream.emit(input.detail.toMap)

  private def ec2InstanceIdStream(details: Map[String, AnyRef]): Stream[Pure, InstanceId] =
    details.getAsStream("EC2InstanceId").map(_.toString).map(tag[InstanceIdTag][String](_))

  def terminatingEc2InstanceId(input: ScheduledEvent): Stream[Pure, InstanceId] =
    for {
      scheduledEvent ← Stream.emit(input)
      details ← scheduledEventDetails(scheduledEvent)
      ec2InstanceId ← ec2InstanceIdStream(details)
    } yield ec2InstanceId

}
