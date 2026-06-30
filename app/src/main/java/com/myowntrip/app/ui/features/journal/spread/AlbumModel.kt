package com.myowntrip.app.ui.features.journal.spread

import androidx.compose.ui.geometry.Offset
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.JournalCanvas
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.JournalPage
import kotlin.math.roundToInt

/**
 * Una hoja del álbum lista para pintar. Combina hoja persistida (o virtual si el día
 * aún no tiene ninguna) con sus piezas. Los días son capítulos: la 1ª hoja del día
 * (pageIndexInDay 0) lleva masthead completo.
 */
data class AlbumPageUi(
  val pageId: String,
  /** false = hoja virtual sin fila en BD todavía (se crea al pegar la 1ª pieza). */
  val isPersisted: Boolean,
  val day: Day,
  val tripId: String,
  val pageIndexInDay: Int,
  val pageCountInDay: Int,
  val notes: List<JournalNote>,
) {
  val isChapterStart: Boolean get() = pageIndexInDay == 0
}

private fun virtualPageId(dayId: String): String = "virtual-$dayId"

fun isVirtualPageId(pageId: String): Boolean = pageId.startsWith("virtual-")

/**
 * Construye la pasada continua del álbum (días ordenados, hojas por día).
 * Días sin hoja persistida muestran UNA hoja virtual en blanco para empezar a pegar.
 */
fun buildAlbumPages(
  days: List<Day>,
  pages: List<JournalPage>,
  notes: List<JournalNote>,
): List<AlbumPageUi> {
  val notesByPage = notes.groupBy { it.pageId }
  val pagesByDay = pages.groupBy { it.dayId }
  return days
    .sortedBy { it.dayNumber }
    .flatMap { day ->
      val dayPages = pagesByDay[day.id].orEmpty().sortedBy { it.pageIndex }
      if (dayPages.isEmpty()) {
        listOf(
          AlbumPageUi(
            pageId = virtualPageId(day.id),
            isPersisted = false,
            day = day,
            tripId = day.tripId,
            pageIndexInDay = 0,
            pageCountInDay = 1,
            notes = notesByPage[null].orEmpty().filter { it.dayId == day.id }
              .sortedBy { it.createdAt },
          ),
        )
      } else {
        dayPages.mapIndexed { index, page ->
          AlbumPageUi(
            pageId = page.id,
            isPersisted = true,
            day = day,
            tripId = page.tripId,
            pageIndexInDay = index,
            pageCountInDay = dayPages.size,
            notes = notesByPage[page.id].orEmpty().sortedBy { it.createdAt },
          )
        }
      }
    }
}

/** Posición de una pieza en el lienzo lógico (dp). null = aún sin colocar. */
fun JournalNote.logicalPosition(): Offset? {
  val x = canvasX ?: return null
  val y = canvasY ?: return null
  return Offset(x, y)
}

/** Redondea una coordenada lógica al paso de rejilla (snap magnético). */
fun snapToGrid(valueDp: Float): Float {
  val step = JournalCanvas.GRID_STEP_DP
  return (valueDp / step).roundToInt() * step
}

/**
 * Rotación de presentación de una pieza: si el usuario ya la colocó (canvasX/Y != null)
 * respeta su [JournalNote.rotation]; si aún no, le da un ladeo "a mano" determinista por
 * id (≈ -6º..6º) para el look scrapbook sin tener que rotar nada manualmente.
 */
fun JournalNote.presentationRotation(): Float {
  if (logicalPosition() != null) return rotation
  val h = id.hashCode()
  val span = ((h % 13) + 13) % 13 // 0..12
  return (span - 6).toFloat() // -6..6
}

/**
 * Auto-flow para piezas sin colocar (canvasX/Y == null): posiciones deterministas
 * en el lienzo lógico, alternando columna, para que no se apilen. No persiste hasta
 * que el usuario arrastra.
 */
fun autoFlowPositions(notes: List<JournalNote>): Map<String, Offset> {
  val result = LinkedHashMap<String, Offset>()
  var y = 16f
  var index = 0
  notes.forEach { note ->
    if (note.logicalPosition() == null) {
      val x = if (index % 2 == 0) 16f else 152f
      result[note.id] = Offset(snapToGrid(x), snapToGrid(y))
      y += 132f
      index++
    }
  }
  return result
}
