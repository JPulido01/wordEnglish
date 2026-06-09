package com.example.wordenglish.data.remote

import com.example.wordenglish.data.remote.dto.DictionaryEntry
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApiService {
    @GET("api/v2/entries/en/{word}")
    suspend fun getDefinition(@Path("word") word: String): List<DictionaryEntry>
}
