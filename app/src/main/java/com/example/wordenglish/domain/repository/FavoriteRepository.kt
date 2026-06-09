package com.example.wordenglish.domain.repository

import com.example.wordenglish.domain.model.Word

interface FavoriteRepository {
    suspend fun getAll(): List<Word>
    suspend fun getById(id: Int): Word?
    suspend fun isFavorite(word: String): Boolean
    suspend fun toggle(word: Word)
}
