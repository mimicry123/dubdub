package com.aroonpa


import com.twitter.bijection.Conversion._
import com.twitter.bijection.twitter_util.UtilBijections.twitter2ScalaFuture
import com.twitter.util.{Future => TwitterFuture}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future => ScalaFuture}


object FutureUtils {

  implicit class FutureImplicit[A](val x: ScalaFuture[A]) {
    def asTwitterFuture: TwitterFuture[A] = x.as[TwitterFuture[A]]
  }

}