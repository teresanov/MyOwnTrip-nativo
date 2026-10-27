package com.myowntrip.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.myowntrip.app.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
  @Query("SELECT * FROM expenses WHERE tripId = :tripId ORDER BY amount DESC")
  fun observeByTrip(tripId: String): Flow<List<ExpenseEntity>>

  @Query("SELECT * FROM expenses WHERE dayId = :dayId")
  fun observeByDay(dayId: String): Flow<List<ExpenseEntity>>

  @Query("SELECT * FROM expenses WHERE id = :id")
  suspend fun getById(id: String): ExpenseEntity?

  @Query("SELECT * FROM expenses WHERE walletEntryId = :walletEntryId LIMIT 1")
  fun observeByWalletEntry(walletEntryId: String): Flow<ExpenseEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(expense: ExpenseEntity)

  @Update
  suspend fun update(expense: ExpenseEntity)

  @Query("DELETE FROM expenses WHERE id = :id")
  suspend fun deleteById(id: String)
}
