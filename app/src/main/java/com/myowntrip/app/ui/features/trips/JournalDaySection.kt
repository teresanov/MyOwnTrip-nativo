package com.myowntrip.app.ui.features.trips

import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.JournalNote

data class JournalDaySection(
  val day: Day,
  val notes: List<JournalNote>,
)

fun buildJournalSections(
  days: List<Day>,
  notes: List<JournalNote>,
): List<JournalDaySection> {
  if (days.isEmpty()) return emptyList()
  val notesByDay = notes.groupBy { it.dayId }
  return days
    .map { day ->
      JournalDaySection(
        day = day,
        notes = notesByDay[day.id].orEmpty().sortedByDescending { it.createdAt },
      )
    }
    .filter { it.notes.isNotEmpty() }
}
