package com.demo.webclient

import com.demo.configuration.Configuration
import com.ning.http.client.{AsyncCompletionHandler, AsyncHttpClient, Response}

import scala.concurrent.{Future, Promise}

object WebClient {
  // here we should pass config later to run it on different thread pool
  private val client = new AsyncHttpClient()
  private val baseUrl = Configuration.apiBaseUrl()

  def get(url: String): Future[Response] = {
    val promise = Promise[Response]()
    val handler = new AsyncCompletionHandler[Response] {
      override def onCompleted(response: Response): Response = {
        promise.success(response)
        response
      }

      override def onThrowable(t: Throwable): Unit = {
        promise.failure(t)
      }
    }
    client.prepareGet(baseUrl + url).execute(handler)
    promise.future
  }
}

