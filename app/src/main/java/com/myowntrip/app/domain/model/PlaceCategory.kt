package com.myowntrip.app.domain.model

/**
 * Tipo de sitio guardado en un viaje. "Restaurante" pasa a ser una categoría más:
 * la sección deja de ser solo gastronomía y se vuelve un directorio de sitios.
 * El nombre interno de la entidad sigue siendo Restaurant por compatibilidad de BD.
 */
enum class PlaceCategory {
  RESTAURANTE,
  CAFE,
  BAR,
  MUSEO,
  MIRADOR,
  MONUMENTO,
  TIENDA,
  SUPERMERCADO,
  PARQUE,
  ALOJAMIENTO,
  OTRO,
}

/**
 * ¿Tiene sentido "reservar" en esta categoría? Solo en sitios donde se reserva mesa o
 * habitación. En el resto, el estado se queda en "Por ir" / "Visitado".
 */
val PlaceCategory.supportsReservation: Boolean
  get() = this == PlaceCategory.RESTAURANTE ||
    this == PlaceCategory.CAFE ||
    this == PlaceCategory.BAR ||
    this == PlaceCategory.ALOJAMIENTO
