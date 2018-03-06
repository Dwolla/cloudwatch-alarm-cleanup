// Generated in part from TypeScript definitions in https://github.com/aws/aws-sdk-js/

package aws

import aws.cloudwatch._

import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js._

@native
@JSImport("aws-sdk", JSImport.Namespace)
object AWS extends Object {

  @native
  class CloudWatch extends Object {

    def describeAlarms(params: DescribeAlarmsInput,
                       callback: Callback[DescribeAlarmsOutput]): Unit = native

    def deleteAlarms(params: DeleteAlarmsInput,
                     callback: Callback[DeleteAlarmsOutput]): Unit = native

  }

}

@native
@JSImport("aws-xray-sdk-core", JSImport.Namespace)
object AWSXRay extends Object {

  def captureAWSClient[T <: Object](aws: T): T = native

}
