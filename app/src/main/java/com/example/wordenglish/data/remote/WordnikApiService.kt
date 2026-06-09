package com.example.wordenglish.data.remote

import com.example.wordenglish.data.remote.dto.WordnikDefinitionEntry
import com.example.wordenglish.data.remote.dto.WordnikExamplesResponse
import com.example.wordenglish.data.remote.dto.WordnikPronunciation
import com.example.wordenglish.data.remote.dto.WordnikRandomWord
import com.example.wordenglish.data.remote.dto.WordnikRelatedWord
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WordnikApiService {

    @GET("words.json/randomWord")
    suspend fun getRandomWord(
        @Query("api_key") apiKey: String,
        @Query("hasDictionaryDef") hasDictionaryDef: Boolean = true,
        @Query("minCorpusCount") minCorpusCount: Int = 5000,
        @Query("minLength") minLength: Int = 4,
        @Query("maxLength") maxLength: Int = 15
    ): WordnikRandomWord

    @GET("word.json/{word}/definitions")
    suspend fun getDefinitions(
        @Path("word") word: String,
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int = 3
    ): List<WordnikDefinitionEntry>

    @GET("word.json/{word}/examples")
    suspend fun getExamples(
        @Path("word") word: String,
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int = 3
    ): WordnikExamplesResponse

    @GET("word.json/{word}/relatedWords")
    suspend fun getRelatedWords(
        @Path("word") word: String,
        @Query("api_key") apiKey: String,
        @Query("relationshipTypes") types: String = "synonym,antonym",
        @Query("limitPerRelationshipType") limitPerRelationshipType: Int = 10
    ): List<WordnikRelatedWord>

    @GET("word.json/{word}/pronunciations")
    suspend fun getPronunciations(
        @Path("word") word: String,
        @Query("api_key") apiKey: String,
        @Query("typeFormat") typeFormat: String = "IPA",
        @Query("limit") limit: Int = 1
    ): List<WordnikPronunciation>
}
