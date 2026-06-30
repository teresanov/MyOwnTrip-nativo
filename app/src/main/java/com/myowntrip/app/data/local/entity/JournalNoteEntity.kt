package com.myowntrip.app.data.local.entity

import androidx.room.ColumnInfo
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
  indices = [Index("dayId"), Index("planBlockId"), Index("pageId")],
)
data class JournalNoteEntity(
  @PrimaryKey val id: String,
  val dayId: String,
  val text: String,
  val pieceType: String,
  val photoUri: String?,
  val audioUri: String?,
  val sketchUri: String?,
  val stickerUri: String?,
  val planBlockId: String?,
  val latitude: Double?,
  val longitude: Double?,
  val createdAt: Long,
  // defaultValue debe coincidir con el DEFAULT de MIGRATION_11_12 o Room rechaza la migración.
  @ColumnInfo(defaultValue = "HAND") val textStyle: String = "HAND",
  // Álbum scrapbook. pageId sin FK a nivel DB (solo índice): añadir FK exigiría
  // recrear la tabla; la integridad se mantiene en el repositorio.
  val pageId: String? = null,
  val canvasX: Float? = null,
  val canvasY: Float? = null,
  // defaultValue debe coincidir con el DEFAULT de MIGRATION_10_11 o Room rechaza la migración.
  @ColumnInfo(defaultValue = "0") val rotation: Float = 0f,
  @ColumnInfo(defaultValue = "1") val scale: Float = 1f,
  @ColumnInfo(defaultValue = "0") val zIndex: Int = 0,
)
