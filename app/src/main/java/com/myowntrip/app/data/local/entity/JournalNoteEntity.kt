package com.myowntrip.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
  tableName = "journal_notes",
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
data class JournalNoteEntity(
  @PrimaryKey val id: String,
  val dayId: String,
  val text: String,
  val photoUri: String?,
  val audioUri: String?,
  val latitude: Double?,
  val longitude: Double?,
  val createdAt: Long,
)
