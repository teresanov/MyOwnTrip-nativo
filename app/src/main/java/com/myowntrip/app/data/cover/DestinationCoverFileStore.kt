package com.myowntrip.app.data.cover

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DestinationCoverFileStore @Inject constructor(
  @ApplicationContext private val context: Context,
) {
  private val coverDir: File
    get() = File(context.filesDir, "destination-covers").also { it.mkdirs() }

  private val client = OkHttpClient.Builder()
    .connectTimeout(20, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .build()

  fun existingPath(cacheKey: String): String? {
    val file = fileForKey(cacheKey)
    return file.takeIf { it.isFile && it.length() > 0L }?.absolutePath
  }

  suspend fun download(cacheKey: String, imageUrl: String): String? = withContext(Dispatchers.IO) {
    val target = fileForKey(cacheKey)
    if (target.exists() && target.length() > 0L) return@withContext target.absolutePath
    val request = Request.Builder()
      .url(imageUrl)
      .header("User-Agent", WikimediaDestinationCoverRemoteDataSource.USER_AGENT)
      .get()
      .build()
    runCatching {
      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) return@withContext null
        val bytes = response.body?.bytes() ?: return@withContext null
        target.writeBytes(bytes)
        target.absolutePath
      }
    }.getOrNull()
  }

  private fun fileForKey(cacheKey: String): File =
    File(coverDir, "$cacheKey.jpg")
}
