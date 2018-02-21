// Generated in part from TypeScript definitions at https://github.com/DefinitelyTyped/DefinitelyTyped/tree/50dafbd8e340f232392f5d7b88d163a046ebfdd5/types/aws-lambda

package aws

import scala.scalajs.js
import js._

package object lambda {
  import lambda._

  type APIGatewayEvent = APIGatewayProxyEvent
  type S3CreateEvent = S3Event
  type CognitoUserPoolEvent = CognitoUserPoolTriggerEvent
  type CloudFormationCustomResourceEvent = CloudFormationCustomResourceCreateEvent | CloudFormationCustomResourceUpdateEvent | CloudFormationCustomResourceDeleteEvent
  type CloudFormationCustomResourceResponse = CloudFormationCustomResourceSuccessResponse | CloudFormationCustomResourceFailedResponse
  type ProxyResult = APIGatewayProxyResult
  type AuthResponse = CustomAuthorizerResult
  type CloudFrontRequestResult = Unit | Null | CloudFrontResultResponse
  type CloudFrontResponseResult = Unit | Null | CloudFrontResultResponse
  type Handler[TEvent, TResult] = js.Function3[TEvent, Context, LambdaCallback[TResult], Unit]
  type LambdaCallback[TResult] = Function2[Error | Null, TResult, Unit]
  type S3Handler = Handler[S3Event, Unit]
  type DynamoDBStreamHandler = Handler[DynamoDBStreamEvent, Unit]
  type SNSHandler = Handler[SNSEvent, Unit]
  type CognitoUserPoolTriggerHandler = Handler[CognitoUserPoolTriggerEvent, js.Any]
  type CloudFormationCustomResourceHandler = Handler[CloudFormationCustomResourceEvent, Unit]
  type CloudWatchLogsHandler = Handler[CloudWatchLogsEvent, Unit]
  type ScheduledHandler = Handler[ScheduledEvent, Unit]
  type APIGatewayProxyHandler = Handler[APIGatewayProxyEvent, APIGatewayProxyResult]
  type APIGatewayProxyLambdaCallback = LambdaCallback[APIGatewayProxyResult]
  type ProxyHandler = APIGatewayProxyHandler
  type ProxyLambdaCallback = APIGatewayProxyLambdaCallback
  type CloudFrontRequestHandler = Handler[CloudFrontRequestEvent, CloudFrontRequestResult]
  type CloudFrontRequestLambdaCallback = LambdaCallback[CloudFrontRequestResult]
  type CloudFrontResponseHandler = Handler[CloudFrontResponseEvent, CloudFrontResponseResult]
  type CloudFrontResponseLambdaCallback = LambdaCallback[CloudFrontResponseResult]
  type CustomAuthorizerHandler = Handler[CustomAuthorizerEvent, CustomAuthorizerResult]
  type CustomAuthorizerLambdaCallback = LambdaCallback[CustomAuthorizerResult]

}
