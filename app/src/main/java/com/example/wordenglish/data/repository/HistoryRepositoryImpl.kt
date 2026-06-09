package com.example.wordenglish.data.repository

import com.example.wordenglish.data.local.HistoryDao
import com.example.wordenglish.data.local.HistoryEntity
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.HistoryRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val MAX_HISTORY = 50

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val dao: HistoryDao
) : HistoryRepository {

    override suspend fun getAll(): List<Word> = dao.getAll().map { it.toDomain() }

    override suspend fun getById(id: Int): Word? = dao.getById(id)?.toDomain()

    override suspend fun add(word: Word) {
        val latest = dao.getLatest()
        if (latest?.word == word.word) return
        if (dao.getCount() >= MAX_HISTORY) dao.deleteOldest()
        dao.insert(word.toEntity())
    }

    private fun HistoryEntity.toDomain() = Word(
        id = id,
        word = word,
        definition = definition,
        ipa = ipa,
        examples = examples?.let { Json.decodeFromString<List<String>>(it) },
        synonyms = synonyms?.let { Json.decodeFromString<List<String>>(it) },
        antonyms = antonyms?.let { Json.decodeFromString<List<String>>(it) }
    )

    private fun Word.toEntity() = HistoryEntity(
        word = word,
        definition = definition,
        ipa = ipa,
        examples = examples?.let { Json.encodeToString(it) },
        synonyms = synonyms?.let { Json.encodeToString(it) },
        antonyms = antonyms?.let { Json.encodeToString(it) }
    )
}
