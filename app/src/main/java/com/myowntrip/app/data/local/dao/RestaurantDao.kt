package com.myowntrip.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myowntrip.app.data.local.entity.RestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
  @Query("SELECT * FROM restaurants WHERE tripId = :tripId ORDER BY name ASC")
  fun observeByTrip(tripId: String): Flow<List<RestaurantEntity>>

  @Query("SELECT * FROM restaurants WHERE id = :id")
  fun observeById(id: String): Flow<RestaurantEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(restaurant: RestaurantEntity)

  @Query("UPDATE restaurants SET status = :status WHERE id = :id")
  suspend fun updateStatus(id: String, status: com.myowntrip.app.domain.model.RestaurantStatus)
}
