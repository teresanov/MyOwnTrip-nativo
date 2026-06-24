package com.myowntrip.app.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.myowntrip.app.data.local.AppDatabase
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
class AppDataRepositoryTest {
  private lateinit var context: Context
  private lateinit var db: AppDatabase
  private lateinit var tripRepository: TripRepository
  private lateinit var appDataRepository: AppDataRepository

  @Before
  fun setup() {
    context = ApplicationProvider.getApplicationContext()
    db = Room.databaseBuilder(context, AppDatabase::class.java, "clear-all-test.db")
      .allowMainThreadQueries()
      .build()
    tripRepository = TripRepository(context, db.tripDao(), db.dayDao())
    appDataRepository = AppDataRepository(context, db)
  }

  @After
  fun tearDown() {
    db.close()
    context.deleteDatabase("clear-all-test.db")
  }

  @Test
  fun clearAllUserData_removesTripsAndLocalTripFiles() = runTest {
    val tripId = tripRepository.createTrip(
      name = "Test",
      destination = "Madrid",
      startDate = LocalDate.of(2026, 6, 1),
      endDate = LocalDate.of(2026, 6, 2),
    )
    val tripDir = context.filesDir.resolve("trips/$tripId")
    tripDir.mkdirs()
    tripDir.resolve("wallet/sample.pdf").apply {
      parentFile?.mkdirs()
      writeText("demo")
    }

    appDataRepository.clearAllUserData()

    assertEquals(0, db.tripDao().count())
    assertTrue(!tripDir.exists())
  }
}
