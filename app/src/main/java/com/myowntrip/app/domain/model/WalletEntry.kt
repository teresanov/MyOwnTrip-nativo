package com.myowntrip.app.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class WalletEntry(
  val id: String,
  val tripId: String,
  val type: EntryType,
  val title: String,
  val date: LocalDate? = null,
  val time: LocalTime? = null,
  val pdfUri: String? = null,
  val linkUrl: String? = null,
  val notes: String? = null,
)
