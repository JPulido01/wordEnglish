package com.example.wordenglish.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {
    @Query("SELECT * FROM words LIMIT 1 OFFSET :index")
    suspend fun getWordByIndex(index: Int): WordEntity?

    @Query("SELECT * FROM words WHERE id = :id")
    suspend fun getWordById(id: Int): WordEntity?

    @Query("SELECT COUNT(*) FROM words")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(words: List<WordEntity>)

    @Query("UPDATE words SET ipa = :ipa, examples = :examples, synonyms = :synonyms, antonyms = :antonyms WHERE id = :id")
    suspend fun updateWordDetails(id: Int, ipa: String?, examples: String?, synonyms: String?, antonyms: String?)
}
