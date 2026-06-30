package com.myowntrip.app.data.repository

import com.myowntrip.app.data.local.dao.JournalPageDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.domain.model.JournalPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JournalPageRepository @Inject constructor(
  private val journalPageDao: JournalPageDao,
) {
  fun observeByTrip(tripId: String): Flow<List<JournalPage>> =
    journalPageDao.observeByTrip(tripId).map { list -> list.map { it.toDomain() } }

  fun observeByDay(dayId: String): Flow<List<JournalPage>> =
    journalPageDao.observeByDay(dayId).map { list -> list.map { it.toDomain() } }

  /** Devuelve la 1ª hoja del día; si no existe, la crea (pageIndex 0). */
  suspend fun ensureDefaultPage(dayId: String, tripId: String): JournalPage {
    journalPageDao.getByDay(dayId).firstOrNull()?.let { return it.toDomain() }
    return addPage(dayId, tripId)
  }

  /** Crea una hoja nueva al final del día (max pageIndex + 1). */
  suspend fun addPage(dayId: String, tripId: String, paperStyle: String? = null): JournalPage {
    val nextIndex = (journalPageDao.maxPageIndexForDay(dayId) ?: -1) + 1
    val page = JournalPage(
      id = UUID.randomUUID().toString(),
      tripId = tripId,
      dayId = dayId,
      pageIndex = nextIndex,
      paperStyle = paperStyle,
    )
    journalPageDao.insert(page.toEntity())
    return page
  }

  suspend fun deletePage(pageId: String) {
    journalPageDao.deleteById(pageId)
  }
}
