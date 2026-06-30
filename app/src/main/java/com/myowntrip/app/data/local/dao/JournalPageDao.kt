package com.myowntrip.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myowntrip.app.data.local.entity.JournalPageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalPageDao {
  @Query(
    """
    SELECT p.* FROM journal_pages p
    INNER JOIN days d ON p.dayId = d.id
    WHERE p.tripId = :tripId
    ORDER BY d.dayNumber ASC, p.pageIndex ASC
    """,
  )
  fun observeByTrip(tripId: String): Flow<List<JournalPageEntity>>

  @Query("SELECT * FROM journal_pages WHERE dayId = :dayId ORDER BY pageIndex ASC")
  fun observeByDay(dayId: String): Flow<List<JournalPageEntity>>

  @Query("SELECT * FROM journal_pages WHERE dayId = :dayId ORDER BY pageIndex ASC")
  suspend fun getByDay(dayId: String): List<JournalPageEntity>

  @Query("SELECT MAX(pageIndex) FROM journal_pages WHERE dayId = :dayId")
  suspend fun maxPageIndexForDay(dayId: String): Int?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(page: JournalPageEntity)

  @Query("DELETE FROM journal_pages WHERE id = :id")
  suspend fun deleteById(id: String)
}
