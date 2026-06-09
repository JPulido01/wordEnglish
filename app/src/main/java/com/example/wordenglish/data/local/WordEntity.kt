package com.example.wordenglish.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey val id: Int,
    val word: String,
    val definition: String,
    val ipa: String? = null,
    val examples: String? = null,
    val synonyms: String? = null,
    val antonyms: String? = null
)
