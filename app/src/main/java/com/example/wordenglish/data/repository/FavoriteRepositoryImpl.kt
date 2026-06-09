package com.example.wordenglish.data.repository

import com.example.wordenglish.data.local.FavoriteDao
import com.example.wordenglish.data.local.FavoriteEntity
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.FavoriteRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val dao: FavoriteDao
) : FavoriteRepository {

    override suspend fun getAll(): List<Word> = dao.getAll().map { it.toDomain() }

    override suspend fun getById(id: Int): Word? = dao.getById(id)?.toDomain()

    override suspend fun isFavorite(word: String): Boolean = dao.isFavorite(word)

    override suspend fun toggle(word: Word) {
        if (dao.isFavorite(word.word)) {
            dao.deleteByWord(word.word)
        } else {
            if (dao.getCount() >= 10) dao.deleteOldest()
            dao.insert(word.toEntity())
        }
    }

    private fun FavoriteEntity.toDomain() = Word(
        id = id,
        word = word,
        definition = definition,
        ipa = ipa,
        examples = examples?.let { Json.decodeFromString<List<String>>(it) },
        synonyms = synonyms?.let { Json.decodeFromString<List<String>>(it) },
        antonyms = antonyms?.let { Json.decodeFromString<List<String>>(it) }
    )

    private fun Word.toEntity() = FavoriteEntity(
        word = word,
        definition = definition,
        ipa = ipa,
        examples = examples?.let { Json.encodeToString(it) },
        synonyms = synonyms?.let { Json.encodeToString(it) },
        antonyms = antonyms?.let { Json.encodeToString(it) }
    )
}
