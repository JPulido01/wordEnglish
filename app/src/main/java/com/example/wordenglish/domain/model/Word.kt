package com.example.wordenglish.domain.model

data class Word(
    val id: Int,
    val word: String,
    val definition: String,
    val ipa: String? = null,
    val examples: List<String>? = null,
    val synonyms: List<String>? = null,
    val antonyms: List<String>? = null
)
