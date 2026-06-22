package com.myowntrip.app.ui.features.trips

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.ui.features.journal.JournalNoteCard
import com.myowntrip.app.ui.theme.MOTSpacing
import com.myowntrip.app.ui.theme.MyOwnTripTheme
import java.time.LocalDate

@Composable
fun TripJournalTab(
  sections: List<JournalDaySection>,
  onDayClick: (String) -> Unit,
  onNoteClick: (String) -> Unit,
  modifier: Modifier = Modifier,
) {
  if (sections.isEmpty()) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(horizontal = MOTSpacing.screenHorizontal, vertical = MOTSpacing.layoutLg),
      verticalArrangement = Arrangement.Center,
    ) {
      Text(
        text = "Aún no hay recuerdos en este viaje",
        style = MaterialTheme.typography.bodyLarge,
      )
      Text(
        text = "Las notas y fotos que añadas aparecerán aquí, agrupadas por día.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = MOTSpacing.componentSm),
      )
    }
    return
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(
      horizontal = MOTSpacing.screenHorizontal,
      vertical = MOTSpacing.layoutMd,
    ),
    verticalArrangement = Arrangement.spacedBy(MOTSpacing.layoutMd),
  ) {
    sections.forEach { section ->
      item(key = "header-${section.day.id}") {
        DaySectionHeader(
          day = section.day,
          onClick = { onDayClick(section.day.id) },
        )
      }
      items(section.notes, key = { it.id }) { note ->
        JournalNoteCard(
          note = note,
          onClick = { onNoteClick(note.id) },
        )
      }
    }
  }
}

@Composable
private fun DaySectionHeader(
  day: Day,
  onClick: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .semantics { contentDescription = "Día ${day.dayNumber}, abrir día" }
      .clickable(onClick = onClick)
      .padding(vertical = MOTSpacing.componentSm),
  ) {
    Text(
      text = "Día ${day.dayNumber}",
      style = MaterialTheme.typography.titleSmall,
      color = MaterialTheme.colorScheme.primary,
    )
    Text(
      text = day.date.toString(),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun TripJournalTabPreview() {
  val day = Day(
    id = "day-1",
    tripId = "trip-1",
    date = LocalDate.of(2026, 6, 12),
    dayNumber = 2,
    title = null,
  )
  val notes = listOf(
    JournalNote(
      id = "n1",
      dayId = day.id,
      text = "Café en la plaza antes del museo.",
      createdAt = System.currentTimeMillis(),
    ),
    JournalNote(
      id = "n2",
      dayId = day.id,
      text = "Atardecer desde el mirador.",
      photoUri = null,
      createdAt = System.currentTimeMillis() - 3_600_000,
    ),
  )
  MyOwnTripTheme {
    TripJournalTab(
      sections = listOf(JournalDaySection(day, notes)),
      onDayClick = {},
      onNoteClick = {},
    )
  }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 400)
@Composable
private fun TripJournalTabEmptyPreview() {
  MyOwnTripTheme {
    TripJournalTab(
      sections = emptyList(),
      onDayClick = {},
      onNoteClick = {},
    )
  }
}
