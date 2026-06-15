package com.myowntrip.app.domain.model

data class ItineraryBlock(
  val id: String,
  val dayId: String,
  val title: String,
  val timeLabel: String? = null,
  val sortOrder: Int,
)
