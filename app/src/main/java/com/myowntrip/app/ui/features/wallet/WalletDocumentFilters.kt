package com.myowntrip.app.ui.features.wallet

import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.WalletEntry
import java.time.LocalDate

enum class WalletDocumentFilterPhase {
  Active,
  Archived,
  All,
}

private val pastTripTypeOrder = mapOf(
  EntryType.FLIGHT to 0,
  EntryType.HOTEL to 1,
  EntryType.ACTIVITY to 2,
  EntryType.TRANSPORT to 3,
  EntryType.GENERIC to 4,
)

fun applyWalletDocumentFilters(
  entries: List<WalletEntry>,
  filterPhase: WalletDocumentFilterPhase,
): List<WalletEntry> = when (filterPhase) {
  WalletDocumentFilterPhase.Active -> entries.filter { !it.isArchived }
  WalletDocumentFilterPhase.Archived -> entries.filter { it.isArchived }
  WalletDocumentFilterPhase.All -> entries
}

fun sortPastTripWalletByType(entries: List<WalletEntry>): List<WalletEntry> =
  entries.sortedWith(
    compareBy<WalletEntry> { pastTripTypeOrder[it.type] ?: Int.MAX_VALUE }
      .thenByDescending { it.date ?: LocalDate.MIN }
      .thenBy { it.title.lowercase() },
  )

fun walletHighlights(entries: List<WalletEntry>): List<WalletEntry> =
  entries
    .filter { !it.isArchived }
    .sortedWith(compareBy<WalletEntry> { it.date == null }.thenBy { it.date })
    .take(4)
