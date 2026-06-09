package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.repository.WordQueueRepository
import javax.inject.Inject

class AdvanceQueueUseCase @Inject constructor(
    private val repository: WordQueueRepository
) {
    suspend operator fun invoke() = repository.advanceQueue()
}
