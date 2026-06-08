package com.myowntrip.app.domain.model

import java.time.LocalDate

data class Trip(
  val id: String,
  val name: String,
  val destination: String,
  val startDate: LocalDate,
  val endDate: LocalDate,
  val coverPhoto: String? = null,
  val createdAt: Long,
)
