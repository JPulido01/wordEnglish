package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(word: Word) = repository.toggle(word)
}
