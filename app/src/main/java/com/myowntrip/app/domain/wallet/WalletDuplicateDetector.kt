package com.myowntrip.app.domain.wallet

import com.myowntrip.app.domain.model.EntryType
import com.myowntrip.app.domain.model.WalletEntry

enum class WalletDuplicateStrength {
  Strong,
  Medium,
}

data class WalletDuplicateMatch(
  val existing: WalletEntry,
  val strength: WalletDuplicateStrength,
)

object WalletDuplicateDetector {
  fun findDuplicate(
    pending: WalletEntry,
    existingEntries: List<WalletEntry>,
    attachmentFileName: String? = null,
    excludeEntryId: String? = null,
  ): WalletDuplicateMatch? {
    val candidates = existingEntries.filter { entry ->
      entry.id != excludeEntryId && !entry.isArchived
    }
    return candidates.firstNotNullOfOrNull { existing ->
      matchStrength(pending, existing, attachmentFileName)?.let { strength ->
        WalletDuplicateMatch(existing = existing, strength = strength)
      }
    }
  }

  private fun matchStrength(
    pending: WalletEntry,
    existing: WalletEntry,
    attachmentFileName: String?,
  ): WalletDuplicateStrength? {
    if (pending.tripId != existing.tripId) return null

    val pendingLink = pending.linkUrl?.normalizeUrl()
    val existingLink = existing.linkUrl?.normalizeUrl()
    if (!pendingLink.isNullOrBlank() && pendingLink == existingLink) {
      return WalletDuplicateStrength.Strong
    }

    val pendingFile = attachmentFileName?.normalizeFileName()
      ?: pending.pdfUri?.fileNameFromUri()?.normalizeFileName()
    val existingFile = existing.pdfUri?.fileNameFromUri()?.normalizeFileName()
    if (!pendingFile.isNullOrBlank() && pendingFile == existingFile) {
      return WalletDuplicateStrength.Strong
    }

    if (
      pending.type == existing.type &&
      pending.title.normalizeTitle() == existing.title.normalizeTitle() &&
      pending.date != null &&
      pending.date == existing.date
    ) {
      return WalletDuplicateStrength.Medium
    }

    return null
  }

  private fun String.normalizeUrl(): String = trim().lowercase()

  private fun String.normalizeFileName(): String =
    substringAfterLast('/').substringAfterLast('\\').trim().lowercase()

  private fun String.normalizeTitle(): String = trim().lowercase()

  private fun String.fileNameFromUri(): String? {
    val path = substringAfter("file://", missingDelimiterValue = this)
    return path.substringAfterLast('/').takeIf { it.isNotBlank() }
  }
}
