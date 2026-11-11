package com.myowntrip.app.data.share

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import javax.inject.Inject
import javax.inject.Singleton

data class ResolvedMapsLink(
  val urls: List<String>,
  val bodies: List<String>,
)

/**
 * Resuelve un enlace corto de Google Maps (maps.app.goo.gl / goo.gl/maps) siguiendo sus
 * redirecciones. El hop 200 de la app móvil suele ser una ficha HTML **sin** `@lat,lng`
 * en la URL: el pin va en el cuerpo (`preview/place/.../@lat,lng` o `!3d!4d`).
 *
 * Best-effort y sin API key: si falla la red, se devuelve lo que se tenga.
 */
@Singleton
class PlaceLinkResolver @Inject constructor() {

  suspend fun resolveChain(startUrl: String): List<String> = resolve(startUrl).urls

  suspend fun resolve(startUrl: String): ResolvedMapsLink = withContext(Dispatchers.IO) {
    val seen = LinkedHashSet<String>()
    val bodies = mutableListOf<String>()
    seen.add(startUrl)
    var current = startUrl
    repeat(MAX_HOPS) {
      val hop = runCatching { fetchHop(current) }.getOrNull()
        ?: return@withContext ResolvedMapsLink(seen.expanded(), bodies)
      hop.location?.let { location ->
        val next = absoluteUrl(current, location)
        if (!seen.add(next)) return@withContext ResolvedMapsLink(seen.expanded(), bodies)
        current = next
        return@repeat
      }
      val body = hop.body
      if (body.isNullOrBlank()) {
        return@withContext ResolvedMapsLink(seen.expanded(), bodies)
      }
      bodies += body
      if (PlaceShareParser.extractLatLngFromText(body) != null) {
        return@withContext ResolvedMapsLink(seen.expanded(), bodies)
      }
      val embedded = nextUrlFromBody(body)
      if (embedded != null) {
        val next = absoluteUrl(current, embedded)
        if (seen.add(next)) {
          current = next
          return@repeat
        }
      }
      return@withContext ResolvedMapsLink(seen.expanded(), bodies)
    }
    ResolvedMapsLink(seen.expanded(), bodies)
  }

  private fun fetchHop(url: String): Hop {
    val conn = (URL(url).openConnection() as HttpURLConnection).apply {
      instanceFollowRedirects = false
      requestMethod = "GET"
      connectTimeout = TIMEOUT_MS
      readTimeout = TIMEOUT_MS
      setRequestProperty("User-Agent", USER_AGENT)
      setRequestProperty("Accept-Language", "es-ES,es;q=0.9,en;q=0.8")
    }
    return try {
      val code = conn.responseCode
      if (code in 300..399) {
        Hop(location = conn.getHeaderField("Location"), body = null)
      } else if (code in 200..299) {
        Hop(location = null, body = readUntilCoords(conn))
      } else {
        Hop(location = null, body = null)
      }
    } finally {
      conn.disconnect()
    }
  }

  private fun readUntilCoords(conn: HttpURLConnection): String {
    val stream = (if (conn.errorStream != null) conn.errorStream else conn.inputStream) ?: return ""
    return stream.bufferedReader().use { reader ->
      val out = StringBuilder(64_000)
      val buf = CharArray(8192)
      var total = 0
      var lastScan = 0
      while (total < MAX_BODY_CHARS) {
        val n = reader.read(buf, 0, minOf(buf.size, MAX_BODY_CHARS - total))
        if (n <= 0) break
        out.append(buf, 0, n)
        total += n
        if (total - lastScan >= SCAN_EVERY_CHARS) {
          if (PlaceShareParser.extractLatLngFromText(out.toString()) != null) break
          lastScan = total
        }
      }
      out.toString()
    }
  }

  private fun nextUrlFromBody(body: String): String? {
    val unescaped = PlaceShareParser.unescapeMapsPayload(body)
    CONTINUE_PARAM.find(unescaped)?.groupValues?.get(1)?.let { raw ->
      val decoded = runCatching { URLDecoder.decode(raw, "UTF-8") }.getOrDefault(raw)
      if (decoded.startsWith("http")) return decoded
    }
    META_REFRESH.find(unescaped)?.groupValues?.get(1)?.let { candidate ->
      if (candidate.startsWith("http")) return candidate
    }
    PREVIEW_PLACE_URL.find(unescaped)?.value?.let { return it }
    return null
  }

  private fun absoluteUrl(current: String, location: String): String =
    if (location.startsWith("http")) location else URL(URL(current), location).toString()

  /** Añade a la cadena las versiones URL-decodificadas (revela `continue=<url codificada>`). */
  private fun Set<String>.expanded(): List<String> = buildList {
    this@expanded.forEach { url ->
      add(url)
      runCatching { URLDecoder.decode(url, "UTF-8") }.getOrNull()?.let { if (it != url) add(it) }
    }
  }

  private data class Hop(val location: String?, val body: String?)

  private companion object {
    const val MAX_HOPS = 8
    const val TIMEOUT_MS = 8_000
    const val MAX_BODY_CHARS = 1_500_000
    const val SCAN_EVERY_CHARS = 24_000
    const val USER_AGENT =
      "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
    val CONTINUE_PARAM = Regex("""(?:\?|&|&amp;)continue=([^&"'\\]+)""")
    val META_REFRESH = Regex("""url=['"]?([^'"\s>]+)""", RegexOption.IGNORE_CASE)
    val PREVIEW_PLACE_URL = Regex("""https://(?:www\.)?google\.[^/\s"']+/maps/preview/place/[^\s"'\\]+""")
  }
}
