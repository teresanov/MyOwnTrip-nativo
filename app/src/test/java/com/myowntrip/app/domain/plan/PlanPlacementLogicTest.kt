package com.myowntrip.app.domain.plan

import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.ItineraryBlock
import com.myowntrip.app.domain.model.WalletEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class PlanPlacementLogicTest {

  private val days = listOf(
    Day("d1", "trip", LocalDate.of(2026, 7, 4), 1, null),
    Day("d2", "trip", LocalDate.of(2026, 7, 5), 2, null),
  )

  @Test
  fun suggest_withDateAndTime_matchesDay() {
    val entry = sampleEntry(date = LocalDate.of(2026, 7, 5), time = LocalTime.of(20, 0))
    val suggestion = PlanPlacementLogic.suggest(entry, days)

    assertTrue(suggestion.canPlace)
    assertEquals("d2", suggestion.dayId)
    assertEquals(PlanPlacementConfidence.HIGH, suggestion.confidence)
    assertTrue(suggestion.summary("Teatro").contains("Día 2"))
    assertTrue(suggestion.summary("Teatro").contains("20:00"))
  }

  @Test
  fun suggest_withoutMatchingDay_cannotPlace() {
    val entry = sampleEntry(date = LocalDate.of(2026, 7, 1), time = null)
    val suggestion = PlanPlacementLogic.suggest(entry, days)

    assertFalse(suggestion.canPlace)
    assertEquals(PlanPlacementConfidence.NONE, suggestion.confidence)
  }

  @Test
  fun insertSorted_ordersByTime() {
    val existing = listOf(
      block("b1", "09:00"),
      block("b2", "18:30"),
    )
    val inserted = PlanPlacementLogic.insertSorted(
      existing,
      block("b3", "11:00"),
    )

    assertEquals(listOf("b1", "b3", "b2"), inserted.map { it.id })
  }

  private fun sampleEntry(date: LocalDate?, time: LocalTime?) = WalletEntry(
    id = "w1",
    tripId = "trip",
    type = EntryType.ACTIVITY,
    title = "Entrada",
    date = date,
    time = time,
  )

  private fun block(id: String, time: String) = ItineraryBlock(
    id = id,
    dayId = "d1",
    title = "Actividad",
    timeLabel = time,
    sortOrder = 0,
  )
}
