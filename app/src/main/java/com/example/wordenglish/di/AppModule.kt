package com.example.wordenglish.di

import com.example.wordenglish.data.repository.FavoriteRepositoryImpl
import com.example.wordenglish.data.repository.HistoryRepositoryImpl
import com.example.wordenglish.data.repository.IntervalRepositoryImpl
import com.example.wordenglish.data.repository.NotificationRepositoryImpl
import com.example.wordenglish.data.repository.WordQueueRepositoryImpl
import com.example.wordenglish.data.repository.WordRepositoryImpl
import com.example.wordenglish.domain.repository.FavoriteRepository
import com.example.wordenglish.domain.repository.HistoryRepository
import com.example.wordenglish.domain.repository.IntervalRepository
import com.example.wordenglish.domain.repository.NotificationRepository
import com.example.wordenglish.domain.repository.WordQueueRepository
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
    abstract fun bindWordQueueRepository(impl: WordQueueRepositoryImpl): WordQueueRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}
