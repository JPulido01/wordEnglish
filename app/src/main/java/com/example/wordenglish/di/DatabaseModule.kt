package com.example.wordenglish.di

import android.content.Context
import androidx.room.Room
import com.example.wordenglish.data.local.WordDao
import com.example.wordenglish.data.local.WordDatabase
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
        Room.databaseBuilder(context, WordDatabase::class.java, "words.db").build()

    @Provides
    fun provideWordDao(db: WordDatabase): WordDao = db.wordDao()
}
