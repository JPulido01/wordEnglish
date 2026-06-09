package com.example.wordenglish.di

import com.example.wordenglish.data.repository.IntervalRepositoryImpl
import com.example.wordenglish.data.repository.WordDetailRepositoryImpl
import com.example.wordenglish.data.repository.WordRepositoryImpl
import com.example.wordenglish.domain.repository.IntervalRepository
import com.example.wordenglish.domain.repository.WordDetailRepository
import com.example.wordenglish.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindWordRepository(impl: WordRepositoryImpl): WordRepository

    @Binds
    @Singleton
    abstract fun bindIntervalRepository(impl: IntervalRepositoryImpl): IntervalRepository

    @Binds
    @Singleton
    abstract fun bindWordDetailRepository(impl: WordDetailRepositoryImpl): WordDetailRepository
}
