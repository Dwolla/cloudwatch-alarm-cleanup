package com.dwolla

import fs2._

import scala.language.higherKinds

package object fs2utils {
  implicit class GetFromMapAsStream[K, V](map: Map[K, V]) {
    def getAsStream(key: K): Stream[Pure, V] =
      Stream.emits(map.get(key).toSeq)
  }
}
