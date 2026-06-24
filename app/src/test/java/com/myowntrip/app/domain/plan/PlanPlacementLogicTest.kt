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
      block("b1", time = "09:00"),
      block("b2", time = "18:30"),
    )
    val inserted = PlanPlacementLogic.insertSorted(
      existing,
      block("b3", time = "11:00"),
    )

    assertEquals(listOf("b1", "b3", "b2"), inserted.map { it.id })
  }

  @Test
  fun sortBlocksForDisplay_usesSortOrder() {
    val blocks = listOf(
      block("b2", sortOrder = 1, time = "18:00"),
      block("b1", sortOrder = 0, time = "09:00"),
    )
    assertEquals(listOf("b1", "b2"), PlanPlacementLogic.sortBlocksForDisplay(blocks).map { it.id })
  }

  @Test
  fun isTimeFixed_flightWithWalletTime() {
    val block = block("b1", sortOrder = 0, time = null)
    val wallet = sampleEntry(time = LocalTime.of(14, 30)).copy(type = EntryType.FLIGHT)
    assertTrue(PlanPlacementLogic.isTimeFixed(block, wallet))
  }

  @Test
  fun isTimeFixed_activityWithoutTime_isFlexible() {
    val block = block("b1", sortOrder = 0, time = null)
    val wallet = sampleEntry(time = null).copy(type = EntryType.ACTIVITY)
    assertFalse(PlanPlacementLogic.isTimeFixed(block, wallet))
  }

  @Test
  fun isTimeFixed_manualTimeWithoutWallet_isFlexible() {
    val block = block("b1", sortOrder = 0, time = "22:00")
    assertFalse(PlanPlacementLogic.isTimeFixed(block, linkedWallet = null))
  }

  @Test
  fun recalculateDaySchedule_manualActivityRecalculatesOnReorder() {
    val blocks = listOf(
      block("breakfast", sortOrder = 0, time = "09:00"),
      block("walk", sortOrder = 1, time = "22:00"),
    )
    val reordered = listOf(
      block("walk", sortOrder = 0, time = "22:00"),
      block("breakfast", sortOrder = 1, time = "09:00"),
    )
    val result = PlanPlacementLogic.recalculateDaySchedule(reordered, emptyList())
    assertEquals(listOf("walk", "breakfast"), result.map { it.id })
    assertEquals("09:00", result[0].timeLabel)
    assertEquals("10:00", result[1].timeLabel)
  }

  @Test
  fun recalculateDaySchedule_flexiblesCascadeFromNine() {
    val blocks = listOf(
      block("walk", sortOrder = 0, time = null),
      block("lunch", sortOrder = 1, time = null),
    )
    val result = PlanPlacementLogic.recalculateDaySchedule(blocks, emptyList())
    assertEquals(listOf("09:00", "10:00"), result.map { it.timeLabel })
  }

  @Test
  fun recalculateDaySchedule_fixedFlightInMiddle() {
    val flightWallet = sampleEntry(time = LocalTime.of(14, 30)).copy(
      id = "w-flight",
      type = EntryType.FLIGHT,
    )
    val blocks = listOf(
      block("walk", sortOrder = 0, time = null),
      block("flight", sortOrder = 1, time = null, walletId = "w-flight"),
      block("taxi", sortOrder = 2, time = null),
    )
    val result = PlanPlacementLogic.recalculateDaySchedule(blocks, listOf(flightWallet))
    assertEquals("09:00", result[0].timeLabel)
    assertEquals("14:30", result[1].timeLabel)
    assertEquals("15:30", result[2].timeLabel)
  }

  @Test
  fun recalculateDaySchedule_museumWithEntryTimeIsFixed() {
    val museumWallet = sampleEntry(time = LocalTime.of(10, 0)).copy(
      id = "w-museum",
      type = EntryType.ACTIVITY,
    )
    val blocks = listOf(
      block("walk", sortOrder = 0, time = null),
      block("museum", sortOrder = 1, time = null, walletId = "w-museum"),
      block("coffee", sortOrder = 2, time = null),
    )
    val result = PlanPlacementLogic.recalculateDaySchedule(blocks, listOf(museumWallet))
    assertEquals("09:00", result[0].timeLabel)
    assertEquals("10:00", result[1].timeLabel)
    assertEquals("11:00", result[2].timeLabel)
  }

  @Test
  fun recalculateDaySchedule_reorderPreservesOrderIndices() {
    val blocks = listOf(
      block("a", sortOrder = 2, time = null),
      block("b", sortOrder = 0, time = null),
      block("c", sortOrder = 1, time = null),
    )
    val result = PlanPlacementLogic.recalculateDaySchedule(blocks, emptyList())
    assertEquals(listOf("b", "c", "a"), result.map { it.id })
    assertEquals(listOf(0, 1, 2), result.map { it.sortOrder })
  }

  private fun sampleEntry(date: LocalDate? = null, time: LocalTime?) = WalletEntry(
    id = "w1",
    tripId = "trip",
    type = EntryType.ACTIVITY,
    title = "Entrada",
    date = date,
    time = time,
  )

  private fun block(
    id: String,
    sortOrder: Int = 0,
    time: String?,
    walletId: String? = null,
  ) = ItineraryBlock(
    id = id,
    dayId = "d1",
    title = "Actividad",
    timeLabel = time,
    sortOrder = sortOrder,
    walletEntryId = walletId,
  )
}
