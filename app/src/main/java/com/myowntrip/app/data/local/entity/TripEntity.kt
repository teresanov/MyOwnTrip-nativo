package com.myowntrip.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "trips")
data class TripEntity(
  @PrimaryKey val id: String,
  val name: String,
  val destination: String,
  val startDate: LocalDate,
  val endDate: LocalDate,
  val coverPhoto: String?,
  val createdAt: Long,
  val archivedAt: Long?,
)
