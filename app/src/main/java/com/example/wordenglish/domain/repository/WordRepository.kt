package com.example.wordenglish.domain.repository

import com.example.wordenglish.domain.model.Word

interface WordRepository {
    suspend fun getWordByIndex(index: Int): Word?
    suspend fun getWordById(id: Int): Word?
    suspend fun getWordCount(): Int
}
