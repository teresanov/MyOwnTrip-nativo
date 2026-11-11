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
 * Último recurso cuando ni la URL ni el HTML de Maps traen un pin. Aproximado:
 * geocodifica el NOMBRE (mejor si va con el destino del viaje), no el punto exacto del POI.
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
