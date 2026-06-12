package com.myowntrip.app.domain.model

data class JournalNote(
  val id: String,
  val dayId: String,
  val text: String,
  val photoUri: String? = null,
  val audioUri: String? = null,
  val latitude: Double? = null,
  val longitude: Double? = null,
  val createdAt: Long,
)
