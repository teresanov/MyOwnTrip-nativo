package com.myowntrip.app.domain.cover

/**
 * Portadas de destino: resolución remota + caché local en disco.
 * La UI solo lee [com.myowntrip.app.domain.model.Trip.coverPhoto] (ruta local).
 */
interface DestinationCoverRepository {
  /** URL remota o ruta `file://` en caché para vista previa al crear viaje. */
  suspend fun lookupPreviewImage(destination: String): String?

  /** Descarga (si hace falta), cachea por destino y persiste en Room. */
  suspend fun attachCoverToTrip(tripId: String, destination: String)

  /** Rellena portadas faltantes en segundo plano (Home, tras crear viaje). */
  suspend fun ensureCoversForTrips(trips: List<com.myowntrip.app.domain.model.Trip>)
}
