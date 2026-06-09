package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.WordQueueRepository
import javax.inject.Inject

class GetCurrentWordUseCase @Inject constructor(
    private val repository: WordQueueRepository
) {
    suspend operator fun invoke(): Word? = repository.getCurrentWord()
}
