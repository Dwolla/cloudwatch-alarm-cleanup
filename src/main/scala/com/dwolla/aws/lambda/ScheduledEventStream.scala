package com.dwolla.aws.lambda

import aws.lambda.ScheduledEvent
import cats.effect.Sync
import com.dwolla.aws.ec2._
import fs2._
import com.dwolla.fs2utils._
import shapeless.tag

import scala.language.higherKinds

object ScheduledEventStream {

  private def scheduledEventDetails[F[_]](input: ScheduledEvent)(implicit F: Sync[F]): Stream[F, Map[String, AnyRef]] =
    Stream.eval(F.delay(input.detail.toMap))

  private def ec2InstanceIdStream[F[_] : Sync](details: Map[String, AnyRef]): Stream[F, InstanceId] =
    details.getAsStream("EC2InstanceId").map(_.toString).map(tag[InstanceIdTag][String](_))

  private def lifecycleTransition[F[_] : Sync](details: Map[String, AnyRef]): Stream[F, String] =
    details.getAsStream("LifecycleTransition").map(_.toString)

  def terminatingEc2InstanceId[F[_] : Sync](input: ScheduledEvent): Stream[F, InstanceId] =
    for {
      scheduledEvent ← Stream.emit(input)
      details ← scheduledEventDetails(scheduledEvent)
      _ ← lifecycleTransition(details).filter(_ == "autoscaling:EC2_INSTANCE_TERMINATING")
      ec2InstanceId ← ec2InstanceIdStream(details)
    } yield ec2InstanceId

}
