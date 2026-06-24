package com.myowntrip.app.data.repository

import android.content.Context
import com.myowntrip.app.data.local.dao.DayDao
import com.myowntrip.app.data.local.dao.TripDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Trip
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.myowntrip.app.ui.features.trips.resolveDefaultJournalDay
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepository @Inject constructor(
  @ApplicationContext private val context: Context,
  private val tripDao: TripDao,
  private val dayDao: DayDao,
) {
  fun observeTrips(): Flow<List<Trip>> =
    tripDao.observeAll().map { list -> list.map { it.toDomain() } }

  fun observeTrip(tripId: String): Flow<Trip?> =
    tripDao.observeById(tripId).map { it?.toDomain() }

  fun observeDays(tripId: String): Flow<List<Day>> =
    dayDao.observeByTrip(tripId).map { list -> list.map { it.toDomain() } }

  fun observeDay(dayId: String): Flow<Day?> =
    dayDao.observeById(dayId).map { it?.toDomain() }

  suspend fun getDaysForTrip(tripId: String): List<Day> =
    dayDao.getByTrip(tripId).map { it.toDomain() }

  suspend fun getTrip(tripId: String): Trip? =
    tripDao.getById(tripId)?.toDomain()

  suspend fun defaultJournalDayId(tripId: String, today: LocalDate = LocalDate.now()): String? {
    val trip = tripDao.getById(tripId)?.toDomain() ?: return null
    val days = dayDao.getByTrip(tripId).map { it.toDomain() }
    return resolveDefaultJournalDay(trip, days, today)?.id
  }

  suspend fun hasTrips(): Boolean = tripDao.count() > 0

  suspend fun getTrips(): List<Trip> = tripDao.getAll().map { it.toDomain() }

  suspend fun findTripByName(name: String): Trip? =
    getTrips().find { it.name == name }

  suspend fun createTrip(
    name: String,
    destination: String,
    startDate: java.time.LocalDate,
    endDate: java.time.LocalDate,
  ): String {
    val tripId = UUID.randomUUID().toString()
    val trip = Trip(
      id = tripId,
      name = name,
      destination = destination,
      startDate = startDate,
      endDate = endDate,
      createdAt = System.currentTimeMillis(),
    )
    tripDao.insert(trip.toEntity())
    generateDays(trip)
    return tripId
  }

  suspend fun updateTrip(
    tripId: String,
    name: String,
    destination: String,
    startDate: LocalDate,
    endDate: LocalDate,
  ) {
    val existing = tripDao.getById(tripId)?.toDomain() ?: return
    val updated = existing.copy(
      name = name.trim(),
      destination = destination.trim(),
      startDate = startDate,
      endDate = endDate,
    )
    tripDao.insert(updated.toEntity())
    syncDays(updated)
  }

  suspend fun deleteTrip(tripId: String) {
    tripDao.deleteById(tripId)
    context.filesDir.resolve("trips/$tripId").deleteRecursively()
  }

  suspend fun archiveTrip(tripId: String) {
    val existing = tripDao.getById(tripId)?.toDomain() ?: return
    tripDao.insert(existing.copy(archivedAt = System.currentTimeMillis()).toEntity())
  }

  suspend fun unarchiveTrip(tripId: String) {
    val existing = tripDao.getById(tripId)?.toDomain() ?: return
    tripDao.insert(existing.copy(archivedAt = null).toEntity())
  }

  private suspend fun generateDays(trip: Trip) {
    val days = buildDaysForTrip(trip, existingDays = emptyList())
    dayDao.insertAll(days.map { it.toEntity() })
  }

  private suspend fun syncDays(trip: Trip) {
    val existing = dayDao.getByTrip(trip.id).map { it.toDomain() }
    val days = buildDaysForTrip(trip, existingDays = existing)
    dayDao.deleteByTrip(trip.id)
    dayDao.insertAll(days.map { it.toEntity() })
  }

  private fun buildDaysForTrip(trip: Trip, existingDays: List<Day>): List<Day> {
    val spanDays = ChronoUnit.DAYS.between(trip.startDate, trip.endDate).toInt() + 1
    val totalDays = spanDays.coerceIn(1, MAX_TRIP_DAYS)
    val sortedExisting = existingDays.sortedBy { it.dayNumber }
    return (0 until totalDays).map { offset ->
      val previous = sortedExisting.getOrNull(offset)
      Day(
        id = previous?.id ?: UUID.randomUUID().toString(),
        tripId = trip.id,
        date = trip.startDate.plusDays(offset.toLong()),
        dayNumber = offset + 1,
        title = previous?.title,
      )
    }
  }

  companion object {
    const val MAX_TRIP_DAYS = 366
  }
}
