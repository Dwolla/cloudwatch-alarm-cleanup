package com.dwolla.awssdk

import com.dwolla.awssdk.ExecuteVia.{Callback => CommonCallback}
import aws.Callback
import aws.cloudwatch.{DescribeAlarmsInput, DescribeAlarmsOutput, PaginationTokenTag}
import com.dwolla.awssdk.PaginatedAwsClient.AwsPaging
import shapeless.tag

import scala.scalajs.js.JavaScriptException

object cloudwatch {
  implicit val describeAlarmsPaging = new AwsPaging[DescribeAlarmsInput, DescribeAlarmsOutput] {
    override def requestForNextPage(gen: () => DescribeAlarmsInput, maybeToken: Option[String]): DescribeAlarmsInput = {
      val a = gen()
      maybeToken.foreach(t => a.NextToken = tag[PaginationTokenTag][String](t))
      a
    }

    override def nextPageToken(res: DescribeAlarmsOutput): Option[String] = Option(res.NextToken.orNull)
  }

  implicit def asyncFunction[Req, Res](func: (Req, Callback[Res]) => Unit): (Req, CommonCallback[Res]) => Unit = { (req, callback: CommonCallback[Res]) =>
    val adaptedCallback: Callback[Res] = (awsErrorOrNull, res) => {
      Option(awsErrorOrNull) match {
        case Some(awsError) => callback(Left(JavaScriptException(awsError)))
        case None => callback(Right(res))
      }
    }
    func(req, adaptedCallback)
  }

}
