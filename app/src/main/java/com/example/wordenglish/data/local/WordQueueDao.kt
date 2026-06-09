package com.example.wordenglish.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordQueueDao {

    @Query("SELECT * FROM word_queue ORDER BY queuePosition ASC LIMIT 1")
    suspend fun getCurrentWord(): WordQueueEntity?

    @Query("SELECT * FROM word_queue WHERE queuePosition BETWEEN 1 AND 10 ORDER BY queuePosition")
    suspend fun getNext10(): List<WordQueueEntity>

    @Query("SELECT COUNT(*) FROM word_queue")
    suspend fun getCount(): Int

    @Query("DELETE FROM word_queue WHERE queuePosition = 0")
    suspend fun removeCurrentWord()

    @Query("UPDATE word_queue SET queuePosition = queuePosition - 1 WHERE queuePosition > 0")
    suspend fun shiftPositionsDown()

    @Insert
    suspend fun insertWord(word: WordQueueEntity)

    @Insert
    suspend fun insertAll(words: List<WordQueueEntity>)

    @Query("UPDATE word_queue SET ipa=:ipa, examples=:examples, synonyms=:synonyms, antonyms=:antonyms WHERE id=:id")
    suspend fun updateDetails(id: Int, ipa: String?, examples: String?, synonyms: String?, antonyms: String?)
}
