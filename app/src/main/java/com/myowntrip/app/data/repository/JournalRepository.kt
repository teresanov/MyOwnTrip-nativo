package com.myowntrip.app.data.repository

import com.myowntrip.app.data.local.dao.JournalNoteDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.domain.model.JournalNote
import com.myowntrip.app.domain.model.JournalPieceType
import com.myowntrip.app.domain.model.JournalTextStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
  private val journalNoteDao: JournalNoteDao,
) {
  fun observeByDay(dayId: String): Flow<List<JournalNote>> =
    journalNoteDao.observeByDay(dayId).map { list -> list.map { it.toDomain() } }

  fun observeByTrip(tripId: String): Flow<List<JournalNote>> =
    journalNoteDao.observeByTrip(tripId).map { list -> list.map { it.toDomain() } }

  fun observeByPage(pageId: String): Flow<List<JournalNote>> =
    journalNoteDao.observeByPage(pageId).map { list -> list.map { it.toDomain() } }

  fun observeNote(noteId: String): Flow<JournalNote?> =
    journalNoteDao.observeById(noteId).map { it?.toDomain() }

  suspend fun getNote(noteId: String): JournalNote? =
    journalNoteDao.getById(noteId)?.toDomain()

  suspend fun addNote(
    dayId: String,
    text: String,
    pieceType: JournalPieceType = JournalPieceType.TEXT,
    photoUri: String? = null,
    audioUri: String? = null,
    sketchUri: String? = null,
    stickerUri: String? = null,
    planBlockId: String? = null,
    latitude: Double? = null,
    longitude: Double? = null,
    textStyle: JournalTextStyle = JournalTextStyle.HAND,
    pageId: String? = null,
    canvasX: Float? = null,
    canvasY: Float? = null,
    rotation: Float = 0f,
    scale: Float = 1f,
    zIndex: Int = 0,
  ) {
    journalNoteDao.insert(
      JournalNote(
        id = UUID.randomUUID().toString(),
        dayId = dayId,
        text = text,
        pieceType = pieceType,
        photoUri = photoUri,
        audioUri = audioUri,
        sketchUri = sketchUri,
        stickerUri = stickerUri,
        planBlockId = planBlockId,
        latitude = latitude,
        longitude = longitude,
        createdAt = System.currentTimeMillis(),
        textStyle = textStyle,
        pageId = pageId,
        canvasX = canvasX,
        canvasY = canvasY,
        rotation = rotation,
        scale = scale,
        zIndex = zIndex,
      ).toEntity(),
    )
  }

  suspend fun updateNote(note: JournalNote) {
    journalNoteDao.insert(note.toEntity())
  }

  /** Persiste la posición/rotación/escala/zIndex de una pieza en el lienzo lógico. */
  suspend fun updateNoteTransform(
    noteId: String,
    canvasX: Float?,
    canvasY: Float?,
    rotation: Float,
    scale: Float,
    zIndex: Int,
  ) {
    journalNoteDao.updateTransform(
      id = noteId,
      canvasX = canvasX,
      canvasY = canvasY,
      rotation = rotation,
      scale = scale,
      zIndex = zIndex,
    )
  }

  suspend fun deleteNote(noteId: String) {
    val note = journalNoteDao.getById(noteId) ?: return
    deleteMediaFiles(note.photoUri, note.audioUri, note.sketchUri, note.stickerUri)
    journalNoteDao.deleteById(noteId)
  }

  private fun deleteMediaFiles(vararg paths: String?) {
    paths.filterNotNull().forEach { path -> File(path).delete() }
  }
}
