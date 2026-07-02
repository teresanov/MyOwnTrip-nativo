package com.myowntrip.app.data.share

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Recuerda, de forma efímera, el viaje del que el usuario salió para buscar un sitio en
 * Google Maps (botón "Buscar en Google Maps"), así al volver por "Compartir" la pantalla
 * de importación preselecciona ESE viaje en vez de adivinar por fecha de creación.
 *
 * Un solo uso: se borra al leerla ([consumeActiveTripId]) o a los [TTL_MS], para que no
 * reaparezca colándose en una importación no relacionada días después.
 */
@Singleton
class LastActiveTripStore @Inject constructor(
  @ApplicationContext context: Context,
) {
  private val prefs = context.getSharedPreferences("place_import_prefs", Context.MODE_PRIVATE)

  fun rememberActiveTrip(tripId: String) {
    prefs.edit {
      putString(KEY_TRIP_ID, tripId)
      putLong(KEY_TIMESTAMP, System.currentTimeMillis())
    }
  }

  /** Lee y consume (borra) el viaje recordado; null si no hay ninguno o ha caducado. */
  fun consumeActiveTripId(): String? {
    val tripId = prefs.getString(KEY_TRIP_ID, null)
    val timestamp = prefs.getLong(KEY_TIMESTAMP, 0L)
    prefs.edit { clear() }
    if (tripId == null) return null
    val age = System.currentTimeMillis() - timestamp
    return if (age in 0..TTL_MS) tripId else null
  }

  private companion object {
    const val KEY_TRIP_ID = "last_active_trip_id"
    const val KEY_TIMESTAMP = "last_active_trip_timestamp"
    const val TTL_MS = 60 * 60 * 1000L // 1h: tiempo de sobra para buscar en Maps y volver.
  }
}
