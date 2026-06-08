package com.myowntrip.app.domain.model

data class JournalNote(
  val id: String,
  val dayId: String,
  val text: String,
  val photoUri: String? = null,
  val createdAt: Long,
)
