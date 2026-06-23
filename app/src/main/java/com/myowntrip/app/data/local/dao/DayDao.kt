package com.myowntrip.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myowntrip.app.data.local.entity.DayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {
  @Query("SELECT * FROM days WHERE tripId = :tripId ORDER BY dayNumber")
  fun observeByTrip(tripId: String): Flow<List<DayEntity>>

  @Query("SELECT * FROM days WHERE tripId = :tripId ORDER BY dayNumber")
  suspend fun getByTrip(tripId: String): List<DayEntity>

  @Query("SELECT * FROM days WHERE id = :id")
  fun observeById(id: String): Flow<DayEntity?>

  @Query("SELECT * FROM days WHERE id = :id")
  suspend fun getById(id: String): DayEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(days: List<DayEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(day: DayEntity)

  @Query("DELETE FROM days WHERE tripId = :tripId")
  suspend fun deleteByTrip(tripId: String)
}
