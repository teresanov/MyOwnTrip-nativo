package com.myowntrip.app.ui.features.trips

import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Trip
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class HomeJournalDayTest {
  private val trip = Trip(
    id = "t1",
    name = "Barcelona",
    destination = "Barcelona",
    startDate = LocalDate.of(2026, 7, 4),
    endDate = LocalDate.of(2026, 7, 6),
    createdAt = 0L,
  )

  private val days = listOf(
    Day("d1", "t1", LocalDate.of(2026, 7, 4), 1),
    Day("d2", "t1", LocalDate.of(2026, 7, 5), 2),
    Day("d3", "t1", LocalDate.of(2026, 7, 6), 3),
  )

  @Test
  fun `prefiere el día de hoy si cae en el viaje`() {
    val result = resolveDefaultJournalDay(trip, days, LocalDate.of(2026, 7, 5))
    assertEquals("d2", result?.id)
  }

  @Test
  fun `viaje próximo usa el primer día`() {
    val result = resolveDefaultJournalDay(trip, days, LocalDate.of(2026, 6, 17))
    assertEquals("d1", result?.id)
  }

  @Test
  fun `viaje pasado usa el último día`() {
    val pastTrip = trip.copy(
      startDate = LocalDate.of(2025, 1, 1),
      endDate = LocalDate.of(2025, 1, 3),
    )
    val pastDays = listOf(
      Day("p1", "t1", LocalDate.of(2025, 1, 1), 1),
      Day("p2", "t1", LocalDate.of(2025, 1, 3), 2),
    )
    val result = resolveDefaultJournalDay(pastTrip, pastDays, LocalDate.of(2026, 6, 17))
    assertEquals("p2", result?.id)
  }

  @Test
  fun `sin días devuelve null`() {
    assertNull(resolveDefaultJournalDay(trip, emptyList()))
  }
}
