# CloudWatch Alarm Cleanup

A [Serverless](https://serverless.com) application for [AWS Lambda](https://aws.amazon.com/lambda/) that will accept [`autoscaling:EC2_INSTANCE_TERMINATING` lifecycle events](https://docs.aws.amazon.com/autoscaling/ec2/userguide/AutoScalingGroupLifecycle.html), identify the EC2 instance ID of the terminating instance, and remove any CloudWatch alarms with an `InstanceId` dimension matching the terminating instance ID.

The application is written in [Scala.JS](https://www.scala-js.org) to be deployed to the Lambda Node runtime.

## Deploy

Run `sbt deploy` to build and deploy the application. The ARN of the Lambda function will be exported by CloudFormation as `cloudwatch-alarm-cleanup:production:RemoveCloudWatchAlarmsArn`. The export can be imported by other CloudFormation stacks to attach the Lambda function as a target for lifecycle events. 

## Credits

AWS Lambda and CloudWatch Scala.js facades were generated from [TypeScript definitions derived from the AWS documentation](https://github.com/DefinitelyTyped/DefinitelyTyped/tree/50dafbd8e340f232392f5d7b88d163a046ebfdd5/types/aws-lambda) and TypeScript definitions included with the [AWS JavaScript SDK](https://github.com/aws/aws-sdk-js/) using the [Scala.js TypeScript importer tool](https://github.com/sjrd/scala-js-ts-importer), and then manually modified to meet the needs of this project.
