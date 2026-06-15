package com.myowntrip.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "itinerary_blocks",
  foreignKeys = [
    ForeignKey(
      entity = DayEntity::class,
      parentColumns = ["id"],
      childColumns = ["dayId"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
  indices = [Index("dayId")],
)
data class ItineraryBlockEntity(
  @PrimaryKey val id: String,
  val dayId: String,
  val title: String,
  val timeLabel: String?,
  val sortOrder: Int,
)
