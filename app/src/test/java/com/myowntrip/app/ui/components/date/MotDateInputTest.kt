package com.myowntrip.app.ui.components.date

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class MotDateInputTest {

  @Test
  fun `parseMotDateInput accepts slash formats`() {
    assertEquals(LocalDate.of(2026, 3, 15), parseMotDateInput("15/03/2026"))
    assertEquals(LocalDate.of(2026, 3, 5), parseMotDateInput("5/3/2026"))
  }

  @Test
  fun `parseMotDateInput accepts formatted display text`() {
    val date = LocalDate.of(2027, 1, 8)
    assertEquals(date, parseMotDateInput(formatMotDate(date)))
  }

  @Test
  fun `parseMotDateInput rejects garbage`() {
    assertNull(parseMotDateInput("no-es-fecha"))
  }

  @Test
  fun `normalizeMotDateRange orders endpoints`() {
    val start = LocalDate.of(2026, 6, 20)
    val end = LocalDate.of(2026, 6, 10)
    assertEquals(end to start, normalizeMotDateRange(start, end))
  }

  @Test
  fun `validateMotDateInput detects out of range for accepted window`() {
    val min = defaultMotMinSelectableDate()
    val max = defaultMotMaxAcceptedTripDate()
    val (validation, _) = validateMotDateInput(formatMotDateInput(max.plusDays(1)), min, max)
    assertEquals(MotDateInputValidation.OutOfRange, validation)
  }

  @Test
  fun `formatMotDateInput uses zero padded day and month`() {
    assertEquals("05/03/2026", formatMotDateInput(LocalDate.of(2026, 3, 5)))
  }
}
