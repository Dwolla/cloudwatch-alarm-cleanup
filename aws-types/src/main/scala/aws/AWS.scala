// Generated in part from TypeScript definitions in https://github.com/aws/aws-sdk-js/

package aws

import aws.cloudwatch._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("aws-sdk", JSImport.Namespace)
object AWS extends js.Object {

  @js.native
  class CloudWatch extends js.Object {

    def describeAlarms(params: DescribeAlarmsInput,
                       callback: Callback[DescribeAlarmsOutput]): Unit = js.native

    def deleteAlarms(params: DeleteAlarmsInput,
                     callback: Callback[DeleteAlarmsOutput]): Unit = js.native

  }

}

@js.native
@JSImport("aws-xray-sdk-core", JSImport.Namespace)
object AWSXRay extends js.Object {

  def captureAWSClient[T <: Object](aws: T): T = js.native

}
