package com.myowntrip.app.data.cover

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wikimedia Commons — sin API key; requiere User-Agent identificable.
 * @see <a href="https://foundation.wikimedia.org/wiki/Policy:Wikimedia_Foundation_User-Agent_Policy">User-Agent policy</a>
 */
@Singleton
class WikimediaDestinationCoverRemoteDataSource @Inject constructor() {

  private val client = OkHttpClient.Builder()
    .connectTimeout(15, TimeUnit.SECONDS)
    .readTimeout(15, TimeUnit.SECONDS)
    .build()

  suspend fun findThumbnailUrl(destination: String): String? = withContext(Dispatchers.IO) {
    val search = "${destination.trim()} city travel"
    val url = buildString {
      append("https://commons.wikimedia.org/w/api.php?")
      append("action=query&format=json&origin=*")
      append("&generator=search&gsrnamespace=6")
      append("&gsrsearch=").append(java.net.URLEncoder.encode(search, Charsets.UTF_8))
      append("&gsrlimit=1")
      append("&prop=pageimages&piprop=thumbnail&pithumbsize=800")
    }
    val request = Request.Builder()
      .url(url)
      .header("User-Agent", USER_AGENT)
      .get()
      .build()
    client.newCall(request).execute().use { response ->
      if (!response.isSuccessful) return@withContext null
      val body = response.body?.string() ?: return@withContext null
      parseThumbnailUrl(body)
    }
  }

  internal fun parseThumbnailUrl(json: String): String? {
    val sourceIndex = json.indexOf(THUMBNAIL_SOURCE_KEY)
    if (sourceIndex < 0) return null
    val valueStart = json.indexOf('"', sourceIndex + THUMBNAIL_SOURCE_KEY.length) + 1
    if (valueStart <= 0) return null
    val valueEnd = json.indexOf('"', valueStart)
    if (valueEnd < 0) return null
    return json.substring(valueStart, valueEnd).takeIf { it.isNotBlank() }
  }

  companion object {
    private const val THUMBNAIL_SOURCE_KEY = """"source":"""
    const val USER_AGENT = "MyOwnTrip/0.1 (Android; travel app; offline-first)"
  }
}
