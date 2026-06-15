package com.myowntrip.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myowntrip.app.data.local.entity.WalletEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletEntryDao {
  @Query("SELECT * FROM wallet_entries WHERE tripId = :tripId ORDER BY date DESC, title")
  fun observeByTrip(tripId: String): Flow<List<WalletEntryEntity>>

  @Query("SELECT * FROM wallet_entries WHERE id = :id")
  fun observeById(id: String): Flow<WalletEntryEntity?>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(entry: WalletEntryEntity)

  @Query("DELETE FROM wallet_entries WHERE id = :id")
  suspend fun deleteById(id: String)
}
