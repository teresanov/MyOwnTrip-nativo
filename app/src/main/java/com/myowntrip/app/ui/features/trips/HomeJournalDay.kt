package com.myowntrip.app.ui.features.trips

import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Trip
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.abs

/** Día por defecto para «Añadir recuerdo» desde Home (viaje destacado). */
fun resolveDefaultJournalDay(
  trip: Trip,
  days: List<Day>,
  today: LocalDate = LocalDate.now(),
): Day? {
  if (days.isEmpty()) return null
  days.find { it.date == today }?.let { return it }
  return when (trip.homePhase(today)) {
    HomeTripPhase.Upcoming -> days.minByOrNull { it.dayNumber }
    HomeTripPhase.Past -> days.maxByOrNull { it.dayNumber }
    HomeTripPhase.Current -> days.minByOrNull { abs(ChronoUnit.DAYS.between(today, it.date)) }
  }
}
