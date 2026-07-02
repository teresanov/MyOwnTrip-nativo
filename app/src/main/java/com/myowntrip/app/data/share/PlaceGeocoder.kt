package com.myowntrip.app.data.share

import android.content.Context
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Geocodifica un NOMBRE de sitio a coordenadas aproximadas usando el Geocoder nativo de
 * Android (backend de Google en dispositivos con Play Services) — gratis, sin API key.
 *
 * Último recurso cuando el enlace de Maps no trae coordenadas literales: desde 2025, al
 * compartir un sitio desde la app móvil de Google Maps, el enlace corto resuelve a una URL
 * con solo un ID interno de Google (hash hexadecimal), no `@lat,lng` — así que no hay nada
 * que extraer de la URL. Aproximado por definición: geocodifica el NOMBRE como texto libre,
 * no el punto exacto del POI.
 */
@Singleton
class PlaceGeocoder @Inject constructor(
  @ApplicationContext private val context: Context,
) {
  suspend fun geocode(query: String): Pair<Double, Double>? {
    if (query.isBlank() || !Geocoder.isPresent()) return null
    return withContext(Dispatchers.IO) {
      runCatching {
        @Suppress("DEPRECATION") // La variante con listener (API 33+) duplicaría esta ruta; esta sigue soportada.
        Geocoder(context, Locale.getDefault())
          .getFromLocationName(query, 1)
          ?.firstOrNull()
          ?.let { it.latitude to it.longitude }
      }.getOrNull()
    }
  }
}
