package com.myowntrip.app.data.share

/**
 * Parseo PURO (sin red) de lo que Google Maps comparte como text/plain. El formato
 * varía por versión/idioma, así que se trabaja a la defensiva: el nombre y el enlace
 * casi siempre salen; las coordenadas dependen de poder resolver el enlace corto
 * (eso lo hace [PlaceLinkResolver], no esto).
 */
object PlaceShareParser {

  private val URL_REGEX = Regex("""https?://\S+""")

  // Patrones de lat,lng dentro de una URL de Maps ya resuelta. De más preciso a menos:
  private val BANG_LATLNG = Regex("""!3d(-?\d+\.\d+)!4d(-?\d+\.\d+)""")
  private val AT_LATLNG = Regex("""@(-?\d+\.\d+),(-?\d+\.\d+)""")
  private val Q_LATLNG = Regex("""[?&](?:q|query|ll|center)=(-?\d+\.\d+),(-?\d+\.\d+)""")

  data class SharedPlace(val name: String?, val url: String?)

  /** Saca nombre + primer enlace del texto/asunto compartido. */
  fun parseShared(text: String?, subject: String? = null): SharedPlace {
    val url = text?.let { URL_REGEX.find(it)?.value }
    val nameFromSubject = subject?.trim()?.takeIf { it.isNotEmpty() }
    val nameFromText = text
      ?.let { if (url != null) it.replace(url, " ") else it }
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
      (BANG_LATLNG.find(url) ?: AT_LATLNG.find(url) ?: Q_LATLNG.find(url))?.let { m ->
        val lat = m.groupValues[1].toDoubleOrNull()
        val lng = m.groupValues[2].toDoubleOrNull()
        if (lat != null && lng != null && lat in -90.0..90.0 && lng in -180.0..180.0) {
          return lat to lng
        }
      }
    }
    return null
  }

  fun extractLatLng(url: String?): Pair<Double, Double>? =
    extractLatLng(listOfNotNull(url))
}
