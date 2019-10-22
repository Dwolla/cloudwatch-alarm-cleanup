package com.dwolla.awssdk

import cats.effect.Effect

object ExecuteVia {
  type Callback[Res] = (Either[Throwable, Res]) => Unit

  implicit class RequestHolder[Req](req: Req) {
    def executeVia[F[_]] = new PartiallyApplied[F]
    final class PartiallyApplied[F[_]] {
      def apply[Res](asyncFunction: (Req, Callback[Res]) => Unit)
                    (implicit F: Effect[F]): F[Res] =
        F.async[Res](asyncFunction(req, _))
    }
  }
}
