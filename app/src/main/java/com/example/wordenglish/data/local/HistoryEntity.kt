package com.example.wordenglish.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val definition: String,
    val ipa: String? = null,
    val examples: String? = null,
    val synonyms: String? = null,
    val antonyms: String? = null,
    val shownAt: Long = System.currentTimeMillis()
)
