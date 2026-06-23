package com.myowntrip.app.ui.features.wallet

import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.WalletEntry
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class WalletDocumentFiltersTest {
  private val entries = listOf(
    WalletEntry(
      id = "1",
      tripId = "t1",
      type = EntryType.FLIGHT,
      title = "Vuelo",
      date = LocalDate.of(2026, 7, 4),
      time = LocalTime.of(9, 15),
    ),
    WalletEntry(
      id = "2",
      tripId = "t1",
      type = EntryType.HOTEL,
      title = "Hotel",
      date = LocalDate.of(2026, 7, 4),
      archivedAt = 1L,
    ),
    WalletEntry(
      id = "3",
      tripId = "t1",
      type = EntryType.ACTIVITY,
      title = "Entrada",
      date = LocalDate.of(2026, 7, 5),
    ),
  )

  @Test
  fun `active filter excludes archived`() {
    val result = applyWalletDocumentFilters(entries, WalletDocumentFilterPhase.Active)
    assertEquals(listOf("1", "3"), result.map { it.id })
  }

  @Test
  fun `archived filter shows only archived`() {
    val result = applyWalletDocumentFilters(entries, WalletDocumentFilterPhase.Archived)
    assertEquals(listOf("2"), result.map { it.id })
  }

  @Test
  fun `all filter includes every entry`() {
    val result = applyWalletDocumentFilters(entries, WalletDocumentFilterPhase.All)
    assertEquals(listOf("1", "2", "3"), result.map { it.id })
  }

  @Test
  fun `highlights exclude archived`() {
    val result = walletHighlights(entries)
    assertEquals(listOf("1", "3"), result.map { it.id })
  }
}
