package com.example.wordenglish.data.repository

import com.example.wordenglish.data.local.WordDao
import com.example.wordenglish.data.remote.DictionaryApiService
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.WordDetailRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordDetailRepositoryImpl @Inject constructor(
    private val apiService: DictionaryApiService,
    private val dao: WordDao
) : WordDetailRepository {

    override suspend fun fetchAndCacheDetails(wordId: Int, word: String): Word {
        val entries = apiService.getDefinition(word)
        val entry = entries.firstOrNull()

        val ipa = entry?.phonetics?.firstOrNull { !it.text.isNullOrBlank() }?.text
        val examples = entry?.meanings
            ?.flatMap { it.definitions }
            ?.mapNotNull { it.example }
            ?.take(3)
            ?.takeIf { it.isNotEmpty() }
        val synonyms = (
            (entry?.meanings?.flatMap { it.synonyms } ?: emptyList()) +
            (entry?.meanings?.flatMap { it.definitions }?.flatMap { it.synonyms } ?: emptyList())
        ).distinct().take(10).takeIf { it.isNotEmpty() }
        val antonyms = (
            (entry?.meanings?.flatMap { it.antonyms } ?: emptyList()) +
            (entry?.meanings?.flatMap { it.definitions }?.flatMap { it.antonyms } ?: emptyList())
        ).distinct().take(10).takeIf { it.isNotEmpty() }

        dao.updateWordDetails(
            id = wordId,
            ipa = ipa,
            examples = examples?.let { Json.encodeToString(it) },
            synonyms = synonyms?.let { Json.encodeToString(it) },
            antonyms = antonyms?.let { Json.encodeToString(it) }
        )

        val entity = dao.getWordById(wordId)
        return Word(
            id = entity?.id ?: wordId,
            word = entity?.word ?: word,
            definition = entity?.definition ?: "",
            ipa = ipa,
            examples = examples,
            synonyms = synonyms,
            antonyms = antonyms
        )
    }
}
