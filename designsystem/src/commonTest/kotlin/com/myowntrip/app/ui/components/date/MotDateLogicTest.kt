package com.myowntrip.app.ui.components.date

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MotDateLogicTest {

  @Test
  fun parseAcceptsSlashFormats() {
    assertEquals(LocalDate(2026, 3, 15), parseMotDateInput("15/03/2026"))
    assertEquals(LocalDate(2026, 3, 5), parseMotDateInput("5/3/2026"))
  }

  @Test
  fun parseAcceptsFormattedDisplayText() {
    val date = LocalDate(2027, 1, 8)
    assertEquals("8 ene 2027", formatMotDate(date))
    assertEquals(date, parseMotDateInput(formatMotDate(date)))
    assertEquals(LocalDate(2026, 9, 1), parseMotDateInput("1 sep 2026"))
    assertEquals(LocalDate(2026, 9, 1), parseMotDateInput("1 Sept. 2026"))
  }

  @Test
  fun parseRejectsGarbageAndImpossibleDates() {
    assertNull(parseMotDateInput("no-es-fecha"))
    assertNull(parseMotDateInput("31/02/2026"))
    assertNull(parseMotDateInput("1/1/26"))
  }

  @Test
  fun normalizeOrdersEndpoints() {
    val start = LocalDate(2026, 6, 20)
    val end = LocalDate(2026, 6, 10)
    assertEquals(end to start, normalizeMotDateRange(start, end))
  }

  @Test
  fun validateDetectsOutOfRangeForAcceptedWindow() {
    val min = defaultMotMinSelectableDate()
    val max = defaultMotMaxAcceptedTripDate()
    val (validation, _) = validateMotDateInput(formatMotDateInput(max.plus(1, DateTimeUnit.DAY)), min, max)
    assertEquals(MotDateInputValidation.OutOfRange, validation)
  }

  @Test
  fun formatInputUsesZeroPaddedDayAndMonth() {
    assertEquals("05/03/2026", formatMotDateInput(LocalDate(2026, 3, 5)))
  }

  @Test
  fun utcMillisRoundTrip() {
    val date = LocalDate(2026, 11, 27)
    assertEquals(date, date.toUtcMillis().toLocalDateUtc())
    assertEquals(LocalDate(1969, 12, 31), (-1L).toLocalDateUtc())
  }

  @Test
  fun timeFormatAndParse() {
    assertEquals("09:05", formatMotTime(LocalTime(9, 5)))
    assertEquals(LocalTime(19, 30), parseMotTime("19:30"))
    assertEquals(LocalTime(9, 30), parseMotTime(" 9:30 "))
    assertNull(parseMotTime("25:00"))
    assertNull(parseMotTime(""))
  }

  @Test
  fun rangeIncludesYearForCrossYearSelection() {
    val formatted = formatMotDateRange(LocalDate(2026, 12, 28), LocalDate(2027, 1, 5))
    assertTrue(formatted.contains("2026"))
    assertTrue(formatted.contains("2027"))
  }

  @Test
  fun applyRangeSelectionStartsNewRangeAfterCompleteSelection() {
    val first = applyRangeSelection(LocalDate(2026, 6, 20), start = null, end = null)
    assertEquals(LocalDate(2026, 6, 20), first.first)
    assertEquals(null, first.second)

    val second = applyRangeSelection(LocalDate(2026, 6, 25), first.first, first.second)
    assertEquals(LocalDate(2026, 6, 20) to LocalDate(2026, 6, 25), second)

    val third = applyRangeSelection(LocalDate(2026, 7, 1), second.first, second.second)
    assertEquals(LocalDate(2026, 7, 1), third.first)
    assertEquals(null, third.second)
  }

  @Test
  fun dayRangeRoleMarksMiddleDays() {
    val start = LocalDate(2026, 6, 20)
    val end = LocalDate(2026, 6, 22)
    assertEquals(MotDayRangeRole.RangeStart, dayRangeRole(start, start, end))
    assertEquals(MotDayRangeRole.RangeMiddle, dayRangeRole(start.plus(1, DateTimeUnit.DAY), start, end))
    assertEquals(MotDayRangeRole.RangeEnd, dayRangeRole(end, start, end))
  }

  @Test
  fun clampYearMonthRespectsBounds() {
    val min = LocalDate(2026, 6, 17)
    val max = LocalDate(2028, 6, 17)
    assertEquals(MotYearMonth(2026, 6), clampYearMonth(MotYearMonth(2025, 12), min, max))
    assertEquals(MotYearMonth(2028, 6), clampYearMonth(MotYearMonth(2029, 1), min, max))
  }

  @Test
  fun yearMonthArithmetic() {
    assertEquals(MotYearMonth(2027, 1), MotYearMonth(2026, 12).plusMonths(1))
    assertEquals(MotYearMonth(2025, 12), MotYearMonth(2026, 1).plusMonths(-1))
    assertEquals(29, MotYearMonth(2028, 2).lengthOfMonth())
    assertEquals(31, MotYearMonth(2026, 12).lengthOfMonth())
  }

  @Test
  fun selectableRangeBlocksPastForTripDefaults() {
    val min = defaultMotMinSelectableDate()
    val max = defaultMotMaxSelectableDate()
    assertFalse(isDateInSelectableRange(min.minus(1, DateTimeUnit.DAY), min, max))
    assertTrue(isDateInSelectableRange(min, min, max))
    assertTrue(isDateInSelectableRange(max, min, max))
    assertFalse(isDateInSelectableRange(max.plus(1, DateTimeUnit.DAY), min, max))
  }

  @Test
  fun monthSelectableRange() {
    val min = LocalDate(2026, 6, 17)
    val max = LocalDate(2026, 8, 2)
    assertFalse(isMonthInSelectableRange(2026, 5, min, max))
    assertTrue(isMonthInSelectableRange(2026, 6, min, max))
    assertTrue(isMonthInSelectableRange(2026, 8, min, max))
    assertFalse(isMonthInSelectableRange(2026, 9, min, max))
  }
}
