package com.example.wordenglish.di

import android.content.Context
import androidx.room.Room
import com.example.wordenglish.data.local.FavoriteDao
import com.example.wordenglish.data.local.WordDao
import com.example.wordenglish.data.local.WordDatabase
import com.example.wordenglish.data.local.WordQueueDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WordDatabase =
        Room.databaseBuilder(context, WordDatabase::class.java, "words.db")
            .addMigrations(WordDatabase.MIGRATION_1_2, WordDatabase.MIGRATION_2_3, WordDatabase.MIGRATION_3_4)
            .build()

    @Provides
    fun provideWordDao(db: WordDatabase): WordDao = db.wordDao()

    @Provides
    fun provideWordQueueDao(db: WordDatabase): WordQueueDao = db.wordQueueDao()

    @Provides
    fun provideFavoriteDao(db: WordDatabase): FavoriteDao = db.favoriteDao()
}
