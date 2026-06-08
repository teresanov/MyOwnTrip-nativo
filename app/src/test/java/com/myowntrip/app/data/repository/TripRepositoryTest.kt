package com.myowntrip.app.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.myowntrip.app.data.local.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class TripRepositoryTest {
  private lateinit var db: AppDatabase
  private lateinit var repository: TripRepository

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
    repository = TripRepository(db.tripDao(), db.dayDao())
  }

  @After
  fun tearDown() {
    db.close()
  }

  @Test
  fun createTrip_generatesDays() = runTest {
    val tripId = repository.createTrip(
      name = "Lisbon",
      destination = "Portugal",
      startDate = LocalDate.of(2026, 6, 1),
      endDate = LocalDate.of(2026, 6, 3),
    )
    val trips = repository.observeTrips().first()
    assertEquals(1, trips.size)
    assertEquals("Lisbon", trips.first().name)
    val days = repository.observeDays(tripId).first()
    assertEquals(3, days.size)
    assertEquals(1, days.first().dayNumber)
  }

  @Test
  fun hasTrips_returnsFalseWhenEmpty() = runTest {
    assertTrue(!repository.hasTrips())
  }
}
