package com.example.wordenglish.data.local.seed

import kotlinx.serialization.Serializable

@Serializable
data class WordSeedItem(
    val id: Int,
    val word: String,
    val definition: String
)
