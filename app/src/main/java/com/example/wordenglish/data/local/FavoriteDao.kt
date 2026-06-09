package com.example.wordenglish.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    suspend fun getAll(): List<FavoriteEntity>

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getById(id: Int): FavoriteEntity?

    @Query("SELECT COUNT(*) FROM favorites")
    suspend fun getCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE word = :word)")
    suspend fun isFavorite(word: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fav: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE word = :word")
    suspend fun deleteByWord(word: String)

    @Query("DELETE FROM favorites WHERE addedAt = (SELECT MIN(addedAt) FROM favorites)")
    suspend fun deleteOldest()
}
