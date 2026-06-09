package com.example.wordenglish.di

import com.example.wordenglish.data.remote.DictionaryApiService
import com.example.wordenglish.data.remote.WordnikApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DictionaryRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WordnikRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    @DictionaryRetrofit
    fun provideDictionaryRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev/")
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    @WordnikRetrofit
    fun provideWordnikRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.wordnik.com/v4/")
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideDictionaryApiService(@DictionaryRetrofit retrofit: Retrofit): DictionaryApiService =
        retrofit.create(DictionaryApiService::class.java)

    @Provides
    @Singleton
    fun provideWordnikApiService(@WordnikRetrofit retrofit: Retrofit): WordnikApiService =
        retrofit.create(WordnikApiService::class.java)
}
