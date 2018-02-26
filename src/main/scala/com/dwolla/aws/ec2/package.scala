package com.dwolla.aws

import shapeless.tag.@@

package object ec2 {

  trait InstanceIdTag

  type InstanceId = String @@ InstanceIdTag

}
