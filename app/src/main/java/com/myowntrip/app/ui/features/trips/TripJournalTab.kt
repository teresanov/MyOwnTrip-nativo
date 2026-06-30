package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.JournalPage
import com.myowntrip.app.domain.model.JournalPieceType
import com.myowntrip.app.ui.features.journal.spread.AlbumPageUi
import com.myowntrip.app.ui.features.journal.spread.JournalSpreadPreviewFrame
import com.myowntrip.app.ui.features.journal.spread.JournalSpreadPreviewTheme
import com.myowntrip.app.ui.features.journal.spread.OnPersistTransform
import com.myowntrip.app.ui.features.journal.spread.ScrapbookAlbum
import com.myowntrip.app.ui.features.journal.spread.buildAlbumPages
import java.time.LocalDate

/**
 * Pestaña Diario del viaje: el álbum completo en una pasada continua. Editable salvo en
 * viajes pasados (arrastrar piezas, nueva hoja, capturar desde el Speed Dial). La pieza
 * nueva cae en la hoja/día que se esté viendo.
 */
@Composable
fun TripJournalTab(
  days: List<Day>,
  pages: List<JournalPage>,
  notes: List<JournalNote>,
  onNoteClick: (String) -> Unit,
  modifier: Modifier = Modifier,
  readOnly: Boolean = true,
  onPersistTransform: OnPersistTransform = { _, _, _, _, _, _ -> },
  onAddSheet: (dayId: String) -> Unit = {},
  onActivePageChange: (AlbumPageUi) -> Unit = {},
) {
  val albumPages = remember(days, pages, notes) {
    buildAlbumPages(days = days, pages = pages, notes = notes)
  }
  ScrapbookAlbum(
    pages = albumPages,
    modifier = modifier.fillMaxSize(),
    readOnly = readOnly,
    onPersistTransform = onPersistTransform,
    onPieceClick = onNoteClick,
    onAddSheet = onAddSheet,
    onActivePageChange = onActivePageChange,
  )
}

@Preview(name = "Diario viaje · álbum", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun TripJournalTabPreview() {
  val day = Day("d1", "t1", LocalDate.of(2026, 6, 12), 2, "Madrid")
  val pages = listOf(JournalPage("p1", "t1", "d1", 0, null))
  val notes = listOf(
    JournalNote(
      id = "n1", dayId = "d1", text = "Café en la plaza antes del museo.",
      pieceType = JournalPieceType.TEXT, createdAt = 1_750_000_000_000,
      pageId = "p1", canvasX = 16f, canvasY = 24f,
    ),
    JournalNote(
      id = "n2", dayId = "d1", text = "Atardecer desde el mirador.",
      pieceType = JournalPieceType.PHOTO, createdAt = 1_750_000_100_000,
      pageId = "p1", canvasX = 150f, canvasY = 200f, rotation = 4f,
    ),
  )
  JournalSpreadPreviewTheme {
    JournalSpreadPreviewFrame(height = 720.dp) {
      TripJournalTab(days = listOf(day), pages = pages, notes = notes, onNoteClick = {})
    }
  }
}

@Preview(name = "Diario viaje · vacío", showBackground = true, widthDp = 360, heightDp = 400)
@Composable
private fun TripJournalTabEmptyPreview() {
  JournalSpreadPreviewTheme {
    JournalSpreadPreviewFrame(height = 400.dp) {
      TripJournalTab(days = emptyList(), pages = emptyList(), notes = emptyList(), onNoteClick = {})
    }
  }
}
