package com.example.wordenglish.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DictionaryEntry(
    val word: String = "",
    val phonetics: List<Phonetic> = emptyList(),
    val meanings: List<Meaning> = emptyList()
)

@Serializable
data class Phonetic(
    val text: String? = null
)

@Serializable
data class Meaning(
    val partOfSpeech: String = "",
    val definitions: List<Definition> = emptyList(),
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
)

@Serializable
data class Definition(
    val definition: String = "",
    val example: String? = null,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList()
)
