package com.myowntrip.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myowntrip.app.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
  @Query("SELECT * FROM expenses WHERE tripId = :tripId ORDER BY amount DESC")
  fun observeByTrip(tripId: String): Flow<List<ExpenseEntity>>

  @Query("SELECT * FROM expenses WHERE dayId = :dayId")
  fun observeByDay(dayId: String): Flow<List<ExpenseEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(expense: ExpenseEntity)
}
