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

  @Query(
    """
    SELECT j.* FROM journal_notes j
    INNER JOIN days d ON j.dayId = d.id
    WHERE d.tripId = :tripId
    ORDER BY d.dayNumber ASC, j.createdAt DESC
    """,
  )
  fun observeByTrip(tripId: String): Flow<List<JournalNoteEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(note: JournalNoteEntity)

  @Query("SELECT * FROM journal_notes WHERE id = :id")
  fun observeById(id: String): Flow<JournalNoteEntity?>

  @Query("SELECT * FROM journal_notes WHERE id = :id")
  suspend fun getById(id: String): JournalNoteEntity?

  @Query("SELECT * FROM journal_notes WHERE pageId = :pageId ORDER BY zIndex ASC, createdAt ASC")
  fun observeByPage(pageId: String): Flow<List<JournalNoteEntity>>

  @Query(
    """
    UPDATE journal_notes
    SET canvasX = :canvasX, canvasY = :canvasY, rotation = :rotation, scale = :scale, zIndex = :zIndex
    WHERE id = :id
    """,
  )
  suspend fun updateTransform(
    id: String,
    canvasX: Float?,
    canvasY: Float?,
    rotation: Float,
    scale: Float,
    zIndex: Int,
  )

  @Query("DELETE FROM journal_notes WHERE id = :id")
  suspend fun deleteById(id: String)
}
