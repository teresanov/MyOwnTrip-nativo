package com.myowntrip.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myowntrip.app.data.local.entity.ItineraryBlockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItineraryBlockDao {
  @Query("SELECT * FROM itinerary_blocks WHERE dayId = :dayId ORDER BY sortOrder ASC")
  fun observeByDay(dayId: String): Flow<List<ItineraryBlockEntity>>

  @Query(
    """
    SELECT itinerary_blocks.* FROM itinerary_blocks
    INNER JOIN days ON itinerary_blocks.dayId = days.id
    WHERE days.tripId = :tripId
    ORDER BY days.dayNumber ASC, itinerary_blocks.sortOrder ASC
    """,
  )
  fun observeByTrip(tripId: String): Flow<List<ItineraryBlockEntity>>

  @Query("SELECT * FROM itinerary_blocks WHERE dayId = :dayId ORDER BY sortOrder ASC")
  suspend fun getByDay(dayId: String): List<ItineraryBlockEntity>

  @Query("SELECT * FROM itinerary_blocks WHERE walletEntryId = :walletEntryId LIMIT 1")
  suspend fun findByWalletEntryId(walletEntryId: String): ItineraryBlockEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(blocks: List<ItineraryBlockEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(block: ItineraryBlockEntity)
}
