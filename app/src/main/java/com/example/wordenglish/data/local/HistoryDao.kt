package com.example.wordenglish.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY shownAt DESC")
    suspend fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM history WHERE id = :id")
    suspend fun getById(id: Int): HistoryEntity?

    @Query("SELECT COUNT(*) FROM history")
    suspend fun getCount(): Int

    @Query("SELECT * FROM history ORDER BY shownAt DESC LIMIT 1")
    suspend fun getLatest(): HistoryEntity?

    @Insert
    suspend fun insert(entry: HistoryEntity)

    @Query("DELETE FROM history WHERE id = (SELECT id FROM history ORDER BY shownAt ASC LIMIT 1)")
    suspend fun deleteOldest()
}
