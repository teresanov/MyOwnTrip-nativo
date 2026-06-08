package com.myowntrip.app.data.repository

import com.myowntrip.app.data.local.dao.DayDao
import com.myowntrip.app.data.local.dao.TripDao
import com.myowntrip.app.data.local.toDomain
import com.myowntrip.app.data.local.toEntity
import com.myowntrip.app.domain.model.Day
import com.myowntrip.app.domain.model.Trip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepository @Inject constructor(
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

  suspend fun hasTrips(): Boolean = tripDao.count() > 0

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

  private suspend fun generateDays(trip: Trip) {
    val totalDays = ChronoUnit.DAYS.between(trip.startDate, trip.endDate).toInt() + 1
    val days = (0 until totalDays).map { offset ->
      val date = trip.startDate.plusDays(offset.toLong())
      Day(
        id = UUID.randomUUID().toString(),
        tripId = trip.id,
        date = date,
        dayNumber = offset + 1,
        title = null,
      )
    }
    dayDao.insertAll(days.map { it.toEntity() })
  }
}
