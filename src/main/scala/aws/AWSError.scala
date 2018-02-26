// Generated in part from TypeScript definitions in https://github.com/aws/aws-sdk-js/

package aws

import scala.scalajs.js
import js._
import scala.scalajs.js.Date
import scala.scalajs.js.annotation._

@js.native
@JSGlobal
class AWSError extends Error {
  var code: String = js.native
  var retryable: Boolean = js.native
  var statusCode: Double = js.native
  var time: Date = js.native
  var hostname: String = js.native
  var region: String = js.native
  var retryDelay: Double = js.native
  var requestId: String = js.native
  var extendedRequstId: String = js.native
  var cfId: String = js.native
}
