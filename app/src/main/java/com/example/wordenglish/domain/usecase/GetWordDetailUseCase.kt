package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.WordDetailRepository
import com.example.wordenglish.domain.repository.WordRepository
import javax.inject.Inject

class GetWordDetailUseCase @Inject constructor(
    private val wordRepository: WordRepository,
    private val detailRepository: WordDetailRepository
) {
    suspend operator fun invoke(wordId: Int): Word? {
        val cached = wordRepository.getWordById(wordId) ?: return null
        if (cached.ipa != null) return cached
        return runCatching { detailRepository.fetchAndCacheDetails(wordId, cached.word) }
            .getOrElse { cached }
    }
}
