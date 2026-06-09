package com.example.wordenglish.worker

import com.example.wordenglish.domain.usecase.AddToHistoryUseCase
import com.example.wordenglish.domain.usecase.AdvanceQueueUseCase
import com.example.wordenglish.domain.usecase.GetCurrentWordUseCase
import com.example.wordenglish.domain.usecase.PreCacheNextWordsUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DailyWordWorkerEntryPoint {
    fun advanceQueueUseCase(): AdvanceQueueUseCase
    fun preCacheNextWordsUseCase(): PreCacheNextWordsUseCase
    fun getCurrentWordUseCase(): GetCurrentWordUseCase
    fun addToHistoryUseCase(): AddToHistoryUseCase
}
