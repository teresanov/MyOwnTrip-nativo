package com.myowntrip.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myowntrip.app.data.local.entity.JournalNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalNoteDao {
  @Query("SELECT * FROM journal_notes WHERE dayId = :dayId ORDER BY createdAt DESC")
  fun observeByDay(dayId: String): Flow<List<JournalNoteEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(note: JournalNoteEntity)
}
