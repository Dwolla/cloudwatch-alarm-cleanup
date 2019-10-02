package com.dwolla.awssdk

import cats.effect.Effect
import cats.implicits._
import com.dwolla.awssdk.ExecuteVia._
import com.dwolla.fs2utils.Pagination
import fs2._

object PaginatedAwsClient {
  type AwsPaging[Req, Res] = Paging[Req, Res, String]

  class PagedAwsClient[F[_] : Effect, Req, Res, T](requestFactory: () => Req)
                                                  (implicit Paging: AwsPaging[Req, Res]) {

    def via(asyncFunction: (Req, Callback[Res]) => Unit)
           (extractor: Res => Seq[T]): Stream[F, T] = {
      val fetchPage = (maybeNextToken: Option[String]) =>
        Paging.requestForNextPage(requestFactory, maybeNextToken)
          .executeVia[F](asyncFunction)
          .map((res: Res) => (Chunk.seq(extractor(res)), Paging.nextPageToken(res)))

      Pagination.offsetUnfoldChunkEval(fetchPage)
    }
  }

  implicit class FetchAll[Req](val requestFactory: () => Req) {
    def fetchAll[F[_]] = new PartiallyApplied[F]

    final class PartiallyApplied[F[_]] {
      def apply[Res, T](asyncFunction: (Req, Callback[Res]) => Unit)
                       (extractor: Res => Seq[T])
                       (implicit F: Effect[F], Paging: AwsPaging[Req, Res]): Stream[F, T] =
        new PagedAwsClient(requestFactory).via(asyncFunction)(extractor)
    }
  }
}

trait Paging[Req, Res, Token] {
  def requestForNextPage(a: () => Req, b: Option[Token]): Req
  def nextPageToken(a: Res): Option[Token]
}
