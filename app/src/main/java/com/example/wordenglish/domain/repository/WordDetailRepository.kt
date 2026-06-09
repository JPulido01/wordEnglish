package com.example.wordenglish.domain.repository

import com.example.wordenglish.domain.model.Word

interface WordDetailRepository {
    suspend fun fetchAndCacheDetails(wordId: Int, word: String): Word
}
