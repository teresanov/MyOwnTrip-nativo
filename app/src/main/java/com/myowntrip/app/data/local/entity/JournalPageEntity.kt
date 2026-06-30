package com.myowntrip.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "journal_pages",
  foreignKeys = [
    ForeignKey(
      entity = DayEntity::class,
      parentColumns = ["id"],
      childColumns = ["dayId"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
  indices = [Index("dayId"), Index("tripId")],
)
data class JournalPageEntity(
  @PrimaryKey val id: String,
  val tripId: String,
  val dayId: String,
  val pageIndex: Int,
  val paperStyle: String?,
)
