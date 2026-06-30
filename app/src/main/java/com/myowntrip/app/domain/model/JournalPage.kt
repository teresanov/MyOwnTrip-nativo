package com.myowntrip.app.domain.model

/**
 * Una hoja del álbum del Diario. Los días son capítulos: la 1ª hoja de un día
 * (pageIndex 0) lleva masthead completo; las siguientes, cabecera ligera.
 */
data class JournalPage(
  val id: String,
  val tripId: String,
  val dayId: String,
  val pageIndex: Int,
  val paperStyle: String? = null,
)
