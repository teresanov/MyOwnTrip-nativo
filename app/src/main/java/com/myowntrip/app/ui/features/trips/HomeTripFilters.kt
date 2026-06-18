package com.myowntrip.app.ui.features.trips

import com.myowntrip.app.domain.model.Trip
import java.time.LocalDate

enum class TripFilterPhase {
  All,
  Current,
  Upcoming,
  Past,
}

enum class TripSortOrder {
  DateUpcoming,
  NameAz,
  DestinationAz,
}

fun applyHomeTripFilters(
  trips: List<Trip>,
  searchQuery: String,
  filterPhase: TripFilterPhase,
  sortOrder: TripSortOrder,
  today: LocalDate = LocalDate.now(),
): List<Trip> {
  val query = searchQuery.trim()
  var result = trips
  if (query.isNotEmpty()) {
    val normalized = query.lowercase()
    result = result.filter { trip ->
      trip.name.lowercase().contains(normalized) ||
        trip.destination.lowercase().contains(normalized)
    }
  }
  result = when (filterPhase) {
    TripFilterPhase.All -> result
    TripFilterPhase.Current -> result.filter { it.homePhase(today) == HomeTripPhase.Current }
    TripFilterPhase.Upcoming -> result.filter { it.homePhase(today) == HomeTripPhase.Upcoming }
    TripFilterPhase.Past -> result.filter { it.homePhase(today) == HomeTripPhase.Past }
  }
  return when (sortOrder) {
    TripSortOrder.DateUpcoming -> sortTripsForHome(result, today)
    TripSortOrder.NameAz -> result.sortedBy { it.name.lowercase() }
    TripSortOrder.DestinationAz -> result.sortedBy { it.destination.lowercase() }
  }
}

internal enum class HomeTripPhase { Current, Upcoming, Past }

internal fun Trip.homePhase(today: LocalDate = LocalDate.now()): HomeTripPhase = when {
  !today.isBefore(startDate) && !today.isAfter(endDate) -> HomeTripPhase.Current
  today.isBefore(startDate) -> HomeTripPhase.Upcoming
  else -> HomeTripPhase.Past
}

internal fun sortTripsForHome(trips: List<Trip>, today: LocalDate = LocalDate.now()): List<Trip> =
  trips.sortedWith(
    compareBy<Trip> { trip ->
      when (trip.homePhase(today)) {
        HomeTripPhase.Current -> 0
        HomeTripPhase.Upcoming -> 1
        HomeTripPhase.Past -> 2
      }
    }.thenBy { trip ->
      when (trip.homePhase(today)) {
        HomeTripPhase.Upcoming -> trip.startDate.toEpochDay()
        HomeTripPhase.Past -> -trip.endDate.toEpochDay()
        HomeTripPhase.Current -> trip.startDate.toEpochDay()
      }
    },
  )

/** Datos de preview alineados al design-file Home (`205:1018`, `228:8161`). */
fun previewHomeTrips(): List<Trip> = listOf(
  Trip(
    id = "1",
    name = "Lisboa en abril",
    destination = "Lisboa",
    startDate = LocalDate.of(2026, 4, 12),
    endDate = LocalDate.of(2026, 4, 18),
    createdAt = 0L,
  ),
  Trip(
    id = "2",
    name = "Barcelona fin de semana",
    destination = "Barcelona",
    startDate = LocalDate.of(2026, 6, 20),
    endDate = LocalDate.of(2026, 6, 22),
    createdAt = 0L,
  ),
  Trip(
    id = "3",
    name = "Tokio otoño",
    destination = "Tokio",
    startDate = LocalDate.of(2025, 11, 1),
    endDate = LocalDate.of(2025, 11, 10),
    createdAt = 0L,
  ),
)
