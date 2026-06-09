package com.example.wordenglish.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WordnikRandomWord(val word: String = "")

@Serializable
data class WordnikDefinitionEntry(
    val text: String? = null,
    val partOfSpeech: String? = null
)

@Serializable
data class WordnikExamplesResponse(val examples: List<WordnikExample> = emptyList())

@Serializable
data class WordnikExample(val text: String? = null)

@Serializable
data class WordnikRelatedWord(
    val relationshipType: String = "",
    val words: List<String> = emptyList()
)

@Serializable
data class WordnikPronunciation(
    val raw: String? = null,
    val rawType: String? = null
)
