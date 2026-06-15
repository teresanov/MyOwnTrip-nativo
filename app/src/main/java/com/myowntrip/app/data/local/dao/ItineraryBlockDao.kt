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

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(blocks: List<ItineraryBlockEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(block: ItineraryBlockEntity)
}
