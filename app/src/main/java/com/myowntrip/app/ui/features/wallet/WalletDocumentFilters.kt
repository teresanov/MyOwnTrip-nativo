package com.myowntrip.app.ui.features.wallet

import com.myowntrip.app.domain.model.WalletEntry
import java.time.LocalDate

enum class WalletDocumentFilterPhase {
  Active,
  Archived,
  All,
}

fun applyWalletDocumentFilters(
  entries: List<WalletEntry>,
  filterPhase: WalletDocumentFilterPhase,
): List<WalletEntry> = when (filterPhase) {
  WalletDocumentFilterPhase.Active -> entries.filter { !it.isArchived }
  WalletDocumentFilterPhase.Archived -> entries.filter { it.isArchived }
  WalletDocumentFilterPhase.All -> entries
}

fun walletHighlights(entries: List<WalletEntry>): List<WalletEntry> =
  entries
    .filter { !it.isArchived }
    .sortedWith(compareBy<WalletEntry> { it.date == null }.thenBy { it.date })
    .take(4)
