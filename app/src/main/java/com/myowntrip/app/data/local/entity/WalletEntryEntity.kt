package com.myowntrip.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myowntrip.app.domain.model.EntryType
import java.time.LocalDate
import java.time.LocalTime

@Entity(
  tableName = "wallet_entries",
  foreignKeys = [
    ForeignKey(
      entity = TripEntity::class,
      parentColumns = ["id"],
      childColumns = ["tripId"],
      onDelete = ForeignKey.CASCADE,
    ),
  ],
  indices = [Index("tripId")],
)
data class WalletEntryEntity(
  @PrimaryKey val id: String,
  val tripId: String,
  val type: EntryType,
  val title: String,
  val date: LocalDate?,
  val time: LocalTime?,
  val pdfUri: String?,
  val linkUrl: String?,
  val notes: String?,
  val qrPayload: String?,
)
