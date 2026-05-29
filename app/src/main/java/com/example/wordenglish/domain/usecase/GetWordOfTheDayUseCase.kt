package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.WordRepository
import java.time.LocalDate
import javax.inject.Inject

class GetWordOfTheDayUseCase @Inject constructor(
    private val repository: WordRepository
) {
    suspend operator fun invoke(): Word? {
        val count = repository.getWordCount()
        if (count == 0) return null
        val index = (LocalDate.now().toEpochDay() % count).toInt()
        return repository.getWordByIndex(index)
    }
}
