package com.myowntrip.app.ui.features.trips

import com.myowntrip.app.domain.model.Trip
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class HomeTripFiltersTest {
  private val today = LocalDate.of(2026, 6, 17)
  private val trips = listOf(
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
      startDate = LocalDate.of(2026, 7, 4),
      endDate = LocalDate.of(2026, 7, 6),
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

  @Test
  fun `search filters by name and destination`() {
    val result = applyHomeTripFilters(
      trips = trips,
      searchQuery = "barcelona",
      filterPhase = TripFilterPhase.All,
      sortOrder = TripSortOrder.DateUpcoming,
      today = today,
    )
    assertEquals(listOf("2"), result.map { it.id })
  }

  @Test
  fun `phase filter keeps only upcoming`() {
    val result = applyHomeTripFilters(
      trips = trips,
      searchQuery = "",
      filterPhase = TripFilterPhase.Upcoming,
      sortOrder = TripSortOrder.DateUpcoming,
      today = today,
    )
    assertEquals(listOf("2"), result.map { it.id })
  }

  @Test
  fun `sort by destination az`() {
    val result = applyHomeTripFilters(
      trips = trips,
      searchQuery = "",
      filterPhase = TripFilterPhase.All,
      sortOrder = TripSortOrder.DestinationAz,
      today = today,
    )
    assertEquals(listOf("Barcelona", "Lisboa", "Tokio"), result.map { it.destination })
  }

  @Test
  fun `default sort puts current then upcoming then past`() {
    val current = trips[0].copy(
      id = "current",
      startDate = today.minusDays(1),
      endDate = today.plusDays(2),
    )
    val result = applyHomeTripFilters(
      trips = trips + current,
      searchQuery = "",
      filterPhase = TripFilterPhase.All,
      sortOrder = TripSortOrder.DateUpcoming,
      today = today,
    )
    assertEquals("current", result.first().id)
    assertTrue(result.indexOfFirst { it.id == "2" } < result.indexOfFirst { it.id == "3" })
  }

  @Test
  fun `hasAnyCurrentOrUpcomingTrips is false when all trips are past`() {
    val onlyPast = trips.filter { it.id != "2" }
    assertEquals(false, hasAnyCurrentOrUpcomingTrips(onlyPast, today))
  }

  @Test
  fun `archived filter excludes active trips and shows only archived`() {
    val archived = trips[2].copy(id = "archived", archivedAt = 1L)
    val all = trips + archived
    val result = applyHomeTripFilters(
      trips = all,
      searchQuery = "",
      filterPhase = TripFilterPhase.Archived,
      sortOrder = TripSortOrder.DateUpcoming,
      today = today,
    )
    assertEquals(listOf("archived"), result.map { it.id })
  }

  @Test
  fun `all filter excludes archived trips`() {
    val archived = trips[0].copy(archivedAt = 1L)
    val result = applyHomeTripFilters(
      trips = trips.map { if (it.id == "1") archived else it },
      searchQuery = "",
      filterPhase = TripFilterPhase.All,
      sortOrder = TripSortOrder.DateUpcoming,
      today = today,
    )
    assertEquals(listOf("2", "3"), result.map { it.id })
  }

  @Test
  fun `hasAnyCurrentOrUpcomingTrips ignores archived trips`() {
    val archivedUpcoming = trips[1].copy(archivedAt = 1L)
    val onlyPastAndArchived = listOf(trips[2], archivedUpcoming)
    assertEquals(false, hasAnyCurrentOrUpcomingTrips(onlyPastAndArchived, today))
  }
}
