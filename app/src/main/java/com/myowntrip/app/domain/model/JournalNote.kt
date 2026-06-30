package com.myowntrip.app.domain.model

data class JournalNote(
  val id: String,
  val dayId: String,
  val text: String,
  val pieceType: JournalPieceType = JournalPieceType.TEXT,
  val photoUri: String? = null,
  val audioUri: String? = null,
  val sketchUri: String? = null,
  val stickerUri: String? = null,
  val planBlockId: String? = null,
  val latitude: Double? = null,
  val longitude: Double? = null,
  val createdAt: Long,
  /** Estilo de la pieza de texto (mano vs máquina). Irrelevante para no-texto. */
  val textStyle: JournalTextStyle = JournalTextStyle.HAND,
  // --- Álbum scrapbook ---
  /** Hoja del álbum donde está pegada la pieza. null = aún sin asignar. */
  val pageId: String? = null,
  /**
   * Posición en el LIENZO LÓGICO de la hoja (no en píxeles de pantalla).
   * La hoja es un espacio fijo de [JournalCanvas.LOGICAL_WIDTH_DP] dp de ancho,
   * escalado para encajar en cada dispositivo, así las coordenadas son portables
   * entre móviles y el export es consistente. Unidades: dp lógicos.
   * null en x o y = pieza sin colocar → auto-flow al abrir la hoja.
   */
  val canvasX: Float? = null,
  val canvasY: Float? = null,
  val rotation: Float = 0f,
  val scale: Float = 1f,
  val zIndex: Int = 0,
)

/** Parámetros del lienzo lógico fijo de una hoja del Diario. */
object JournalCanvas {
  /** Ancho del lienzo lógico en dp. Las coordenadas de pieza viven en este espacio. */
  const val LOGICAL_WIDTH_DP = 360f

  /** Paso de la rejilla magnética, en dp lógicos. */
  const val GRID_STEP_DP = 18f
}
