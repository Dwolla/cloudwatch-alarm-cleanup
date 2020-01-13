// Generated in part from TypeScript definitions in https://github.com/aws/aws-sdk-js/

package aws

import shapeless.tag.@@

import scala.scalajs.js._

package object cloudwatch {

  type HKUnit[_] = Unit

  type AlarmName = String @@ AlarmNameTag

  type AlarmNames = Array[AlarmName]
  type AlarmNamePrefix = String
  type StateValue = String
  type ActionPrefix = String
  type MaxRecords = Double
  type NextToken = String @@ PaginationTokenTag
  type MetricAlarms = Array[MetricAlarm]
  type AlarmArn = String @@ ArnTag
  type AlarmDescription = String
  type Timestamp = Date
  type ActionsEnabled = Boolean
  type ResourceList = Array[ResourceName]
  type ResourceName = String
  type StateReason = String
  type StateReasonData = String
  type MetricName = String
  type Namespace = String
  type Statistic = String
  type Statistics = Array[Statistic]
  type ExtendedStatistic = String
  type ExtendedStatistics = Array[ExtendedStatistic]
  type Dimensions = Array[Dimension]
  type Period = Double
  type StandardUnit = String
  type EvaluationPeriods = Double
  type DatapointsToAlarm = Double
  type Threshold = Double
  type ComparisonOperator = String
  type TreatMissingData = String
  type EvaluateLowSampleCountPercentile = String
  type DimensionName = String
  type DimensionValue = String

}
