package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.IntervalRepository
import com.example.wordenglish.domain.repository.WordRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import javax.inject.Inject

class GetWordOfTheDayUseCase @Inject constructor(
    private val repository: WordRepository,
    private val intervalRepository: IntervalRepository
) {
    suspend operator fun invoke(): Word? {
        val count = repository.getWordCount()
        if (count == 0) return null
        val interval = intervalRepository.interval.first()
        val index = if (interval == null) {
            (LocalDate.now().toEpochDay() % count).toInt()
        } else {
            val epochHour = System.currentTimeMillis() / (1000L * 3600)
            ((epochHour / interval.hours) % count).toInt()
        }
        return repository.getWordByIndex(index)
    }
}
