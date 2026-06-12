package com.myowntrip.app.data.repository

import com.myowntrip.app.data.local.dao.JournalNoteDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.domain.model.JournalNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalRepository @Inject constructor(
  private val journalNoteDao: JournalNoteDao,
) {
  fun observeByDay(dayId: String): Flow<List<JournalNote>> =
    journalNoteDao.observeByDay(dayId).map { list -> list.map { it.toDomain() } }

  suspend fun addNote(
    dayId: String,
    text: String,
    photoUri: String? = null,
    audioUri: String? = null,
    latitude: Double? = null,
    longitude: Double? = null,
  ) {
    journalNoteDao.insert(
      JournalNote(
        id = UUID.randomUUID().toString(),
        dayId = dayId,
        text = text,
        photoUri = photoUri,
        audioUri = audioUri,
        latitude = latitude,
        longitude = longitude,
        createdAt = System.currentTimeMillis(),
      ).toEntity(),
    )
  }
}
