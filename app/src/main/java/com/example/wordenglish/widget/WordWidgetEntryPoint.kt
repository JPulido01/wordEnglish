package com.example.wordenglish.widget

import com.example.wordenglish.domain.usecase.GetCurrentWordUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WordWidgetEntryPoint {
    fun getCurrentWordUseCase(): GetCurrentWordUseCase
}
