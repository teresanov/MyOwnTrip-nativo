package com.myowntrip.app.domain.wallet

import com.myowntrip.app.domain.model.EntryType
import java.time.LocalDate
import java.time.LocalTime

enum class ParseConfidence {
  HIGH,
  LOW,
  FAILED,
}

data class ParsedWalletDocument(
  val type: EntryType,
  val title: String,
  val date: LocalDate? = null,
  val time: LocalTime? = null,
  val notes: String? = null,
  val qrPayload: String? = null,
  val confidence: ParseConfidence,
) {
  val parseFailed: Boolean
    get() = confidence == ParseConfidence.FAILED
}
