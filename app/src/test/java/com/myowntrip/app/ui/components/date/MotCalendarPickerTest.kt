package com.myowntrip.app.ui.components.date

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class MotCalendarPickerTest {

  @Test
  fun `formatMotDateRange includes year for cross-year selection`() {
    val start = LocalDate.of(2026, 12, 28)
    val end = LocalDate.of(2027, 1, 5)
    val formatted = formatMotDateRange(start, end)
    assertTrue(formatted.contains("2026"))
    assertTrue(formatted.contains("2027"))
  }

  @Test
  fun `applyRangeSelection starts new range after complete selection`() {
    val first = applyRangeSelection(
      date = LocalDate.of(2026, 6, 20),
      start = null,
      end = null,
    )
    assertEquals(LocalDate.of(2026, 6, 20), first.first)
    assertEquals(null, first.second)

    val second = applyRangeSelection(
      date = LocalDate.of(2026, 6, 25),
      start = first.first,
      end = first.second,
    )
    assertEquals(LocalDate.of(2026, 6, 20) to LocalDate.of(2026, 6, 25), second)

    val third = applyRangeSelection(
      date = LocalDate.of(2026, 7, 1),
      start = second.first,
      end = second.second,
    )
    assertEquals(LocalDate.of(2026, 7, 1), third.first)
    assertEquals(null, third.second)
  }

  @Test
  fun `dayRangeRole marks middle days in range`() {
    val start = LocalDate.of(2026, 6, 20)
    val end = LocalDate.of(2026, 6, 22)
    assertEquals(MotDayRangeRole.RangeStart, dayRangeRole(start, start, end))
    assertEquals(MotDayRangeRole.RangeMiddle, dayRangeRole(start.plusDays(1), start, end))
    assertEquals(MotDayRangeRole.RangeEnd, dayRangeRole(end, start, end))
  }

  @Test
  fun `clampYearMonth respects bounds`() {
    val min = LocalDate.of(2026, 6, 17)
    val max = LocalDate.of(2028, 6, 17)
    assertEquals(YearMonth.of(2026, 6), clampYearMonth(YearMonth.of(2025, 12), min, max))
    assertEquals(YearMonth.of(2028, 6), clampYearMonth(YearMonth.of(2029, 1), min, max))
  }

  @Test
  fun `isDateInSelectableRange blocks past for trip defaults`() {
    val min = defaultMotMinSelectableDate()
    val max = defaultMotMaxSelectableDate()
    assertFalse(isDateInSelectableRange(min.minusDays(1), min, max))
    assertTrue(isDateInSelectableRange(min, min, max))
    assertTrue(isDateInSelectableRange(max, min, max))
    assertFalse(isDateInSelectableRange(max.plusDays(1), min, max))
  }
}
