package com.dwolla.aws

import shapeless.tag.@@

package object ec2 {
  type InstanceId = String @@ InstanceIdTag
}

package ec2 {
  trait InstanceIdTag
}
