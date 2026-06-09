package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.HistoryRepository
import javax.inject.Inject

class AddToHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(word: Word) = repository.add(word)
}
