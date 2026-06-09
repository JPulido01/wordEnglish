package com.example.wordenglish.domain.repository

import com.example.wordenglish.domain.model.WordInterval
import kotlinx.coroutines.flow.Flow

interface IntervalRepository {
    val interval: Flow<WordInterval?>
    suspend fun setInterval(interval: WordInterval)
}
