package com.myowntrip.app.data.share

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Resuelve un enlace corto de Google Maps (maps.app.goo.gl / goo.gl/maps) siguiendo sus
 * redirecciones para llegar a la URL larga, que sí lleva las coordenadas (@lat,lng o
 * !3d!4d). Best-effort y sin API key: si falla la red, se devuelve lo que se tenga.
 *
 * Devuelve TODA la cadena de URLs vista (más sus versiones decodificadas), porque en la UE
 * Maps puede redirigir a una página de consentimiento con la URL real metida en `continue=`.
 */
@Singleton
class PlaceLinkResolver @Inject constructor() {

  suspend fun resolveChain(startUrl: String): List<String> = withContext(Dispatchers.IO) {
    val seen = LinkedHashSet<String>()
    seen.add(startUrl)
    var current = startUrl
    repeat(MAX_HOPS) {
      val location = runCatching { redirectLocationOf(current) }.getOrNull() ?: return@withContext seen.expanded()
      val next = if (location.startsWith("http")) location else URL(URL(current), location).toString()
      if (!seen.add(next)) return@withContext seen.expanded()
      current = next
    }
    seen.expanded()
  }

  private fun redirectLocationOf(url: String): String? {
    val conn = (URL(url).openConnection() as HttpURLConnection).apply {
      instanceFollowRedirects = false
      requestMethod = "GET"
      connectTimeout = TIMEOUT_MS
      readTimeout = TIMEOUT_MS
      setRequestProperty("User-Agent", USER_AGENT)
    }
    return try {
      if (conn.responseCode in 300..399) conn.getHeaderField("Location") else null
    } finally {
      conn.disconnect()
    }
  }

  /** Añade a la cadena las versiones URL-decodificadas (revela `continue=<url codificada>`). */
  private fun Set<String>.expanded(): List<String> = buildList {
    this@expanded.forEach { url ->
      add(url)
      runCatching { URLDecoder.decode(url, "UTF-8") }.getOrNull()?.let { if (it != url) add(it) }
    }
  }

  private companion object {
    const val MAX_HOPS = 5
    const val TIMEOUT_MS = 8_000
    const val USER_AGENT = "Mozilla/5.0 (Linux; Android) MyOwnTrip"
  }
}
