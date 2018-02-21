package com.dwolla

import cats.effect.Sync
import fs2._

import scala.language.higherKinds

package object fs2utils {
  implicit class GetFromMapAsStream[K, V](map: Map[K, V]) {
    def getAsStream[F[_]](key: K)(implicit F: Sync[F]): Stream[F, V] =
      Stream.eval(F.delay(map.get(key).toSeq))
        .flatMap(Stream.emits(_).covary[F])
  }
}
