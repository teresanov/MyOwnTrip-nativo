package com.myowntrip.app.domain.model

import java.time.LocalDate

data class Day(
  val id: String,
  val tripId: String,
  val date: LocalDate,
  val dayNumber: Int,
  val title: String? = null,
)
