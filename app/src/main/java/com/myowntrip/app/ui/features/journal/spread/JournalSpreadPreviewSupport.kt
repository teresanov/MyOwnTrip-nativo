package com.myowntrip.app.ui.features.journal.spread

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.JournalPieceType
import com.myowntrip.app.ui.theme.MyOwnTripPreviewTheme
import java.time.LocalDate

/** Marco 360×H para previews — evita `fillMaxSize()` sin restricción en el panel de diseño. */
@Composable
fun JournalSpreadPreviewFrame(
  height: Dp = 800.dp,
  width: Dp = 360.dp,
  content: @Composable () -> Unit,
) {
  Box(
    modifier = Modifier
      .width(width)
      .height(height),
  ) {
    content()
  }
}

@Composable
fun JournalSpreadPreviewTheme(content: @Composable () -> Unit) {
  MyOwnTripPreviewTheme(content = content)
}

object JournalSpreadPreviewSamples {
  val day = Day(
    id = "preview-day",
    tripId = "preview-trip",
    date = LocalDate.of(2026, 6, 12),
    dayNumber = 3,
    title = "Madrid",
  )

  val planBlock = ItineraryBlock(
    id = "preview-block",
    dayId = "preview-day",
    title = "Museo del Prado",
    timeLabel = "11:00",
    sortOrder = 0,
  )

  val mixedNotes: List<JournalNote> = listOf(
    JournalNote(
      id = "preview-note-photo",
      dayId = day.id,
      text = "La luz en la sala de Goya.",
      pieceType = JournalPieceType.PHOTO,
      photoUri = "preview-missing-photo.jpg",
      createdAt = 1_750_000_000_000,
    ),
    JournalNote(
      id = "preview-note-event",
      dayId = day.id,
      text = "Impresionante la colección permanente.",
      pieceType = JournalPieceType.EVENT_BOOKMARK,
      planBlockId = planBlock.id,
      createdAt = 1_749_996_400_000,
    ),
    JournalNote(
      id = "preview-note-audio",
      dayId = day.id,
      text = "Sensación de calma en el jardín.",
      pieceType = JournalPieceType.AUDIO,
      audioUri = "preview-missing-audio.m4a",
      createdAt = 1_749_992_800_000,
    ),
    JournalNote(
      id = "preview-note-text",
      dayId = day.id,
      text = "Café con leche en la esquina antes de volver.",
      pieceType = JournalPieceType.TEXT,
      createdAt = 1_749_989_200_000,
    ),
  )
}
