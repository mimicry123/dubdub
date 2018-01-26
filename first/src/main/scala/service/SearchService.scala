package com.aroonpa.service

import java.util.Calendar

import com.aroonpa.FutureUtils._
import com.aroonpa.Media
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.google.inject.Inject
import com.sksamuel.elastic4s.http.ElasticDsl._
import com.sksamuel.elastic4s.http.search.{SearchHit, SearchResponse}
import com.sksamuel.elastic4s.http.{HttpClient, RequestFailure, RequestSuccess}
import com.sksamuel.elastic4s.{ElasticsearchClientUri, RefreshPolicy}
import com.twitter.inject.annotations.Flag
import com.twitter.util.{Future => TwitterFuture}


class SearchService @Inject()(@Flag("elasticHost") host: String = "localhost",
                              @Flag("elasticPort") port: Int = 9200) extends AutoCloseable {

  val client = HttpClient(ElasticsearchClientUri(host, port))
  client.execute {
    createIndex("media").mappings(
      mapping("video")
    )
  }.await


  val media = Media(20, "video", "Onnum", 1, 1, 1, now, now(), "current", 50, 1, 1, 1, 1, 1, 1, "endra", "a@g.com", "dubbed", now())


  def store(media: Media) {
    client.execute {
      indexInto("media" / "video").doc(JsonUtil.toJson(media)).refresh(RefreshPolicy.IMMEDIATE)
    }
  }

  def searchKey(key: String, from: Int = 0, size: Int = 10): TwitterFuture[Array[SearchHit]] = {
    // now we can search for the document we just indexed
    val resp = client.execute {
      search("media") from (from) size (size) query key
    }
    val response: TwitterFuture[Either[RequestFailure, RequestSuccess[SearchResponse]]] = resp.asTwitterFuture
    response.flatMap({
      _ match {
        case Left(failure) => TwitterFuture.exception(new Exception(failure.toString))
        case Right(results) => TwitterFuture.value(results.result.hits.hits)
      }
    })
  }

  private def now() = {
    new java.sql.Date(Calendar.getInstance().getTimeInMillis())
  }

  override def close(): Unit = client.close()
}


object JsonUtil {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k, v) => k.name -> v })
  }

  def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def toMap[V](json: String)(implicit m: Manifest[V]) = fromJson[Map[String, V]](json)

  def fromJson[T](json: String)(implicit m: Manifest[T]): T = {
    mapper.readValue[T](json)
  }
}