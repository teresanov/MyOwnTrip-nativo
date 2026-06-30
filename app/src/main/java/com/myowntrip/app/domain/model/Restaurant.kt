package com.myowntrip.app.domain.model

/**
 * Un sitio guardado en un viaje (restaurante, museo, mirador…). El nombre interno
 * sigue siendo Restaurant por compatibilidad con la tabla `restaurants`; de cara al
 * usuario es un "Sitio". Los campos de coordenadas/contacto se rellenan solos cuando
 * el sitio llega por "Compartir desde Google Maps"; al añadirlo a mano quedan null.
 */
data class Restaurant(
  val id: String,
  val tripId: String,
  val dayId: String? = null,
  val name: String,
  val address: String? = null,
  val status: RestaurantStatus = RestaurantStatus.WITHOUT_RESERVATION,
  val notes: String? = null,
  val category: PlaceCategory = PlaceCategory.OTRO,
  // --- Datos de mapa/contacto (se autorrellenan al compartir desde Maps) ---
  val latitude: Double? = null,
  val longitude: Double? = null,
  /** Place ID de Google, si lo conocemos. Permite reconsultar detalles más adelante. */
  val placeId: String? = null,
  val phone: String? = null,
  val website: String? = null,
  /** Enlace canónico para abrir el sitio en la app de Google Maps. */
  val googleMapsUri: String? = null,
  val rating: Double? = null,
) {
  /** ¿Tenemos coordenadas para pintar pin / abrir en Maps? */
  val hasLocation: Boolean get() = latitude != null && longitude != null
}
