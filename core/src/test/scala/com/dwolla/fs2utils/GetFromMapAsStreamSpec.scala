package com.dwolla.fs2utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpecLike

class GetFromMapAsStreamSpec extends AnyFlatSpecLike with Matchers {

  behavior of "GetFromMapAsStream"

  it should "emit a single value found in the map" in {
    val value = Map("key" -> "value").getAsStream("key")

    value.toList should be(List("value"))
  }

  it should "return an empty list if the key is missing" in {
    val value = Map.empty[String, Any].getAsStream("key")

    value.toList should be(List.empty[Any])
  }

}
