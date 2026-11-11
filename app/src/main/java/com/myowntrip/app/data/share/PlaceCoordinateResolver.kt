package com.myowntrip.app.data.share

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Saca lat/lng de un share de Maps. Si hay enlace, el pin sale de la URL/HTML:
 * nunca se mezcla con el destino del viaje (si no, un café de Las Rozas acaba en Londres).
 * El geocoder solo entra cuando no hay enlace, p. ej. un nombre tecleado a mano.
 */
@Singleton
class PlaceCoordinateResolver @Inject constructor(
  private val linkResolver: PlaceLinkResolver,
  private val placeGeocoder: PlaceGeocoder,
) {

  suspend fun resolve(
    url: String?,
    name: String?,
    destinationHint: String? = null,
  ): Pair<Double, Double>? {
    if (!url.isNullOrBlank()) {
      return resolveFromMapsLink(url)
    }

    val trimmedName = name?.trim().orEmpty()
    if (trimmedName.isEmpty()) return null
    val hint = destinationHint?.trim().orEmpty()
    if (hint.isNotEmpty()) {
      placeGeocoder.geocode("$trimmedName, $hint")?.let { return it }
    }
    return placeGeocoder.geocode(trimmedName)
  }

  /** Solo el pin del enlace de Maps. Sin geocodificar el nombre ni el destino. */
  suspend fun resolveFromMapsLink(url: String): Pair<Double, Double>? {
    PlaceShareParser.extractLatLng(url)?.let { return it }
    val resolved = linkResolver.resolve(url)
    PlaceShareParser.extractLatLng(resolved.urls)?.let { return it }
    resolved.bodies.forEach { body ->
      PlaceShareParser.extractLatLngFromText(body)?.let { return it }
    }
    return null
  }
}
