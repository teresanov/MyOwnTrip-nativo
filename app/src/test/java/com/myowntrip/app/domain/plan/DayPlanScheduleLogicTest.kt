package com.myowntrip.app.domain.plan

import com.myowntrip.app.domain.model.ItineraryBlock
import org.junit.Assert.assertEquals
import org.junit.Test

class DayPlanScheduleLogicTest {

  @Test
  fun gridHourRange_spansBlockTimes() {
    val blocks = listOf(
      block("a", "08:30"),
      block("b", "22:00"),
    )
    assertEquals(7..23, DayPlanScheduleLogic.gridHourRange(blocks, emptyList()))
  }

  @Test
  fun resortAfterTimeEdit_insertsByTime() {
    val blocks = listOf(
      block("a", "09:00", 0),
      block("b", "18:00", 1),
    )
    val edited = block("b", "11:00", 1)
    val result = DayPlanScheduleLogic.resortAfterTimeEdit(
      blocks = blocks.map { if (it.id == "b") edited else it },
      editedBlockId = "b",
      walletEntries = emptyList(),
    )
    assertEquals(listOf("a", "b"), result.map { it.id })
    assertEquals(listOf(0, 1), result.map { it.sortOrder })
  }

  @Test
  fun snapMinutes_roundsToQuarterHour() {
    assertEquals(9 * 60 + 15, DayPlanScheduleLogic.snapMinutes(9 * 60 + 17))
    assertEquals(10 * 60, DayPlanScheduleLogic.snapMinutes(10 * 60 + 7))
  }

  @Test
  fun minutesToTimeLabel_formatsCorrectly() {
    assertEquals("09:15", DayPlanScheduleLogic.minutesToTimeLabel(9 * 60 + 15))
  }

  private fun block(id: String, time: String, sortOrder: Int = 0) = ItineraryBlock(
    id = id,
    dayId = "d1",
    title = "Actividad",
    timeLabel = time,
    sortOrder = sortOrder,
  )
}
