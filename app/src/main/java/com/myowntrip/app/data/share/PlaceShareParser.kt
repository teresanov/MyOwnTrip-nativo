package com.myowntrip.app.data.share

/**
 * Parseo PURO (sin red) de lo que Google Maps comparte como text/plain. El formato
 * varía por versión/idioma, así que se trabaja a la defensiva: el nombre y el enlace
 * casi siempre salen; las coordenadas dependen de poder resolver el enlace corto
 * (eso lo hace [PlaceLinkResolver], no esto).
 */
object PlaceShareParser {

  private val URL_REGEX = Regex("""https?://\S+""")

  // Patrones de lat,lng. De más preciso (pin del POI) a menos (viewport).
  private val PLACE_PIN = Regex("""!8m2!3d(-?\d+\.\d+)!4d(-?\d+\.\d+)""")
  private val BANG_LATLNG = Regex("""!3d(-?\d+\.\d+)!4d(-?\d+\.\d+)""")
  /** Cámara Maps: !2d = lng, !3d = lat. */
  private val BANG_LNG_LAT = Regex("""!2d(-?\d+\.\d+)!3d(-?\d+\.\d+)""")
  private val PREVIEW_AT = Regex("""/maps/preview/place/[^"'\\]+/@(-?\d+\.\d+),(-?\d+\.\d+)""")
  private val AT_LATLNG = Regex("""@(-?\d+\.\d+),(-?\d+\.\d+)""")
  private val SEARCH_PATH = Regex("""/maps/search/(-?\d+\.\d+)\s*,\s*\+?(-?\d+\.\d+)""")
  private val Q_LATLNG =
    Regex("""[?&](?:q|query|ll|center|destination|origin)=(-?\d+\.\d+)\s*,\s*\+?(-?\d+\.\d+)""")
  private val GEO_LATLNG = Regex("""geo:(-?\d+\.\d+)\s*,\s*(-?\d+\.\d+)""", RegexOption.IGNORE_CASE)
  private val JSON_NULL_PAIR = Regex("""\[null,null,(-?\d+\.\d+),(-?\d+\.\d+)\]""")
  private val JSON_LAT_LNG =
    Regex(""""lat(?:itude)?"\s*:\s*(-?\d+\.\d+)\s*,\s*"(?:lng|lon|longitude)"\s*:\s*(-?\d+\.\d+)""")
  private val UNICODE_ESCAPE = Regex("""\\u([0-9a-fA-F]{4})""")

  data class SharedPlace(val name: String?, val url: String?)

  /** Saca nombre + primer enlace del texto/asunto compartido. */
  fun parseShared(text: String?, subject: String? = null): SharedPlace {
    val rawUrl = text?.let { URL_REGEX.find(it)?.value }
    val url = rawUrl?.let { sanitizeUrl(it) }
    val nameFromSubject = subject?.trim()?.takeIf { it.isNotEmpty() }
    val nameFromText = text
      ?.let { if (rawUrl != null) it.replace(rawUrl, " ") else it }
      ?.lineSequence()
      ?.map { it.trim() }
      ?.firstOrNull { it.isNotEmpty() }
    // Si el "nombre" es una frase de relleno tipo "Mira ... en Google Maps", lo dejamos
    // igual: el usuario lo edita en la pantalla de confirmación.
    return SharedPlace(name = nameFromSubject ?: nameFromText, url = url)
  }

  /** Intenta extraer (lat, lng) de cualquiera de las URLs candidatas. null si no hay. */
  fun extractLatLng(urls: List<String>): Pair<Double, Double>? {
    urls.forEach { url ->
      matchCoords(unescapeMapsPayload(url))?.let { return it }
    }
    return null
  }

  fun extractLatLng(url: String?): Pair<Double, Double>? =
    extractLatLng(listOfNotNull(url))

  /**
   * Coordenadas embebidas en HTML o JSON de Maps (enlace corto de la app móvil: la URL
   * final no trae @lat,lng; el pin vive en el cuerpo, a menudo con `\u003d`).
   */
  fun extractLatLngFromText(text: String): Pair<Double, Double>? {
    val unescaped = unescapeMapsPayload(text)
    return matchCoords(unescaped)
  }

  internal fun unescapeMapsPayload(text: String): String {
    val unicode = UNICODE_ESCAPE.replace(text) { match ->
      val code = match.groupValues[1].toIntOrNull(16) ?: return@replace match.value
      code.toChar().toString()
    }
    return unicode
      .replace("\\/", "/")
      .replace("%21", "!", ignoreCase = true)
      .replace("%40", "@", ignoreCase = true)
      .replace("%2C", ",", ignoreCase = true)
      .replace("%2B", "+", ignoreCase = true)
  }

  private fun matchCoords(source: String): Pair<Double, Double>? {
    PLACE_PIN.find(source)?.let { coordsOf(it)?.let { pair -> return pair } }
    PREVIEW_AT.find(source)?.let { coordsOf(it)?.let { pair -> return pair } }
    SEARCH_PATH.find(source)?.let { coordsOf(it)?.let { pair -> return pair } }
    BANG_LATLNG.find(source)?.let { coordsOf(it)?.let { pair -> return pair } }
    BANG_LNG_LAT.find(source)?.let { match ->
      val lng = match.groupValues[1].toDoubleOrNull() ?: return@let
      val lat = match.groupValues[2].toDoubleOrNull() ?: return@let
      validCoords(lat, lng)?.let { return it }
    }
    AT_LATLNG.find(source)?.let { coordsOf(it)?.let { pair -> return pair } }
    Q_LATLNG.find(source)?.let { coordsOf(it)?.let { pair -> return pair } }
    GEO_LATLNG.find(source)?.let { coordsOf(it)?.let { pair -> return pair } }
    JSON_NULL_PAIR.find(source)?.let { coordsOf(it)?.let { pair -> return pair } }
    JSON_LAT_LNG.find(source)?.let { coordsOf(it)?.let { pair -> return pair } }
    return null
  }

  private fun coordsOf(match: MatchResult): Pair<Double, Double>? {
    val lat = match.groupValues[1].toDoubleOrNull() ?: return null
    val lng = match.groupValues[2].toDoubleOrNull() ?: return null
    return validCoords(lat, lng)
  }

  private fun validCoords(lat: Double, lng: Double): Pair<Double, Double>? {
    if (lat !in -90.0..90.0 || lng !in -180.0..180.0) return null
    if (lat == 0.0 && lng == 0.0) return null
    return lat to lng
  }

  private fun sanitizeUrl(raw: String): String =
    raw.trim().trimEnd('.', ',', ';', ')', ']', '"', '\'')
}
