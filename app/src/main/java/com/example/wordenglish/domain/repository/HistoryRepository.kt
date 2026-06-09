package com.example.wordenglish.domain.repository

import com.example.wordenglish.domain.model.Word

interface HistoryRepository {
    suspend fun getAll(): List<Word>
    suspend fun getById(id: Int): Word?
    suspend fun add(word: Word)
}
