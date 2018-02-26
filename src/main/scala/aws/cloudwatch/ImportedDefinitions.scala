// Generated in part from TypeScript definitions in https://github.com/aws/aws-sdk-js/

package aws.cloudwatch

import scala.scalajs.js._
import scala.scalajs.js.JSConverters._

@native
trait DescribeAlarmsInput extends Object {
  var AlarmNames: AlarmNames = native
  var AlarmNamePrefix: AlarmNamePrefix = native
  var StateValue: StateValue = native
  var ActionPrefix: ActionPrefix = native
  var MaxRecords: MaxRecords = native
  var NextToken: NextToken = native
}

object DescribeAlarmsInput {
  def apply(): DescribeAlarmsInput = Dynamic.literal().asInstanceOf[DescribeAlarmsInput]
}

@native
trait DescribeAlarmsOutput extends Object {
  var MetricAlarms: MetricAlarms = native
  var NextToken: UndefOr[NextToken] = native
}

@native
trait MetricAlarm extends Object {
  var AlarmName: AlarmName = native
  var AlarmArn: AlarmArn = native
  var AlarmDescription: AlarmDescription = native
  var AlarmConfigurationUpdatedTimestamp: Timestamp = native
  var ActionsEnabled: ActionsEnabled = native
  var OKActions: ResourceList = native
  var AlarmActions: ResourceList = native
  var InsufficientDataActions: ResourceList = native
  var StateValue: StateValue = native
  var StateReason: StateReason = native
  var StateReasonData: StateReasonData = native
  var StateUpdatedTimestamp: Timestamp = native
  var MetricName: MetricName = native
  var Namespace: Namespace = native
  var Statistic: Statistic = native
  var ExtendedStatistic: ExtendedStatistic = native
  var Dimensions: Dimensions = native
  var Period: Period = native
  var Unit: StandardUnit = native
  var EvaluationPeriods: EvaluationPeriods = native
  var DatapointsToAlarm: DatapointsToAlarm = native
  var Threshold: Threshold = native
  var ComparisonOperator: ComparisonOperator = native
  var TreatMissingData: TreatMissingData = native
  var EvaluateLowSampleCountPercentile: EvaluateLowSampleCountPercentile = native
}

@native
trait Dimension extends Object {
  var Name: DimensionName = native
  var Value: DimensionValue = native
}

@native
trait DeleteAlarmsInput extends Object {
  var AlarmNames: AlarmNames = native
}

object DeleteAlarmsInput {
  def apply(alarms: AlarmName*): DeleteAlarmsInput = {
    val deleteAlarmsInput = Dynamic.literal().asInstanceOf[DeleteAlarmsInput]
    deleteAlarmsInput.AlarmNames = alarms.toJSArray
    deleteAlarmsInput
  }
}

@native
trait DeleteAlarmsOutput extends Object
