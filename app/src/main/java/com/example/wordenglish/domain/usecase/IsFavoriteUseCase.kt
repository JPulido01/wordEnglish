package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.repository.FavoriteRepository
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(word: String): Boolean = repository.isFavorite(word)
}
