// Generated in part from TypeScript definitions in https://github.com/aws/aws-sdk-js/

package aws.cloudwatch

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr

@js.native
trait DescribeAlarmsInput extends js.Object {
  var AlarmNames: AlarmNames = js.native
  var AlarmNamePrefix: AlarmNamePrefix = js.native
  var StateValue: StateValue = js.native
  var ActionPrefix: ActionPrefix = js.native
  var MaxRecords: MaxRecords = js.native
  var NextToken: NextToken = js.native
}

object DescribeAlarmsInput {
  def apply(): DescribeAlarmsInput = js.Dynamic.literal().asInstanceOf[DescribeAlarmsInput]
}

@js.native
trait DescribeAlarmsOutput extends js.Object {
  var MetricAlarms: MetricAlarms = js.native
  var NextToken: UndefOr[NextToken] = js.native
}

@js.native
trait MetricAlarm extends js.Object {
  var AlarmName: AlarmName = js.native
  var AlarmArn: AlarmArn = js.native
  var AlarmDescription: AlarmDescription = js.native
  var AlarmConfigurationUpdatedTimestamp: Timestamp = js.native
  var ActionsEnabled: ActionsEnabled = js.native
  var OKActions: ResourceList = js.native
  var AlarmActions: ResourceList = js.native
  var InsufficientDataActions: ResourceList = js.native
  var StateValue: StateValue = js.native
  var StateReason: StateReason = js.native
  var StateReasonData: StateReasonData = js.native
  var StateUpdatedTimestamp: Timestamp = js.native
  var MetricName: MetricName = js.native
  var Namespace: Namespace = js.native
  var Statistic: Statistic = js.native
  var ExtendedStatistic: ExtendedStatistic = js.native
  var Dimensions: Dimensions = js.native
  var Period: Period = js.native
  var Unit: StandardUnit = js.native
  var EvaluationPeriods: EvaluationPeriods = js.native
  var DatapointsToAlarm: DatapointsToAlarm = js.native
  var Threshold: Threshold = js.native
  var ComparisonOperator: ComparisonOperator = js.native
  var TreatMissingData: TreatMissingData = js.native
  var EvaluateLowSampleCountPercentile: EvaluateLowSampleCountPercentile = js.native
}

@js.native
trait Dimension extends js.Object {
  var Name: DimensionName = js.native
  var Value: DimensionValue = js.native
}

@js.native
trait DeleteAlarmsInput extends js.Object {
  var AlarmNames: AlarmNames = js.native
}

object DeleteAlarmsInput {
  def apply(alarms: AlarmName*): DeleteAlarmsInput = {
    val deleteAlarmsInput = js.Dynamic.literal().asInstanceOf[DeleteAlarmsInput]
    deleteAlarmsInput.AlarmNames = alarms.toJSArray
    deleteAlarmsInput
  }
}

@js.native
trait DeleteAlarmsOutput extends js.Object
