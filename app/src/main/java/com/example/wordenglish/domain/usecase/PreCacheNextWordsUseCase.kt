package com.example.wordenglish.domain.usecase

import com.example.wordenglish.data.local.WordQueueDao
import com.example.wordenglish.domain.repository.WordQueueRepository
import javax.inject.Inject

class PreCacheNextWordsUseCase @Inject constructor(
    private val queueDao: WordQueueDao,
    private val repository: WordQueueRepository
) {
    suspend operator fun invoke() {
        val wordsToCache = queueDao.getNext10()
            .filter { it.ipa == null && it.examples == null && it.synonyms == null }
        for (word in wordsToCache) {
            runCatching { repository.preCacheDetails(word.id, word.word) }
        }
    }
}
