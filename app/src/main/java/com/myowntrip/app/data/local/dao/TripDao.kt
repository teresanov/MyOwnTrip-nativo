package com.myowntrip.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myowntrip.app.data.local.entity.TripEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
  @Query("SELECT * FROM trips ORDER BY startDate DESC")
  fun observeAll(): Flow<List<TripEntity>>

  @Query("SELECT * FROM trips WHERE id = :id")
  fun observeById(id: String): Flow<TripEntity?>

  @Query("SELECT COUNT(*) FROM trips")
  suspend fun count(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(trip: TripEntity)

  @Query("DELETE FROM trips WHERE id = :id")
  suspend fun deleteById(id: String)
}
