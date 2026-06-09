package com.example.wordenglish.data.repository

import androidx.room.withTransaction
import com.example.wordenglish.data.local.WordDatabase
import com.example.wordenglish.data.remote.WORDNIK_API_KEY
import com.example.wordenglish.data.local.WordQueueDao
import com.example.wordenglish.data.local.WordQueueEntity
import com.example.wordenglish.data.remote.WordnikApiService
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.WordQueueRepository
import com.example.wordenglish.domain.repository.WordRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordQueueRepositoryImpl @Inject constructor(
    private val database: WordDatabase,
    private val queueDao: WordQueueDao,
    private val wordRepository: WordRepository,
    private val wordnikApi: WordnikApiService
) : WordQueueRepository {

    override suspend fun getCurrentWord(): Word? =
        queueDao.getCurrentWord()?.toDomain()

    override suspend fun getCount(): Int =
        queueDao.getCount()

    override suspend fun initializeIfEmpty() {
        if (queueDao.getCount() > 0) return
        val count = wordRepository.getWordCount()  // triggers ensureSeeded()
        if (count == 0) return
        val queueWords = (0 until count).take(100).mapNotNull { index ->
            wordRepository.getWordByIndex(index)?.let { word ->
                WordQueueEntity(
                    word = word.word,
                    definition = word.definition,
                    queuePosition = index
                )
            }
        }
        if (queueWords.isNotEmpty()) {
            queueDao.insertAll(queueWords)
        }
    }

    override suspend fun advanceQueue() {
        val apiKey = WORDNIK_API_KEY
        if (apiKey.isBlank()) return

        val randomWord = runCatching { wordnikApi.getRandomWord(apiKey) }.getOrNull() ?: return
        val definitions = runCatching { wordnikApi.getDefinitions(randomWord.word, apiKey) }.getOrNull()
        val definition = definitions?.firstOrNull()?.text?.stripHtml()?.takeIf { it.isNotBlank() } ?: return

        database.withTransaction {
            queueDao.removeCurrentWord()
            queueDao.shiftPositionsDown()
            queueDao.insertWord(
                WordQueueEntity(
                    word = randomWord.word,
                    definition = definition,
                    queuePosition = 99
                )
            )
        }
    }

    override suspend fun preCacheDetails(queueId: Int, wordText: String) {
        val apiKey = WORDNIK_API_KEY
        if (apiKey.isBlank()) return

        val ipa = runCatching {
            wordnikApi.getPronunciations(wordText, apiKey).firstOrNull()?.raw
        }.getOrNull()

        val examples = runCatching {
            wordnikApi.getExamples(wordText, apiKey).examples
                .mapNotNull { it.text?.takeIf { t -> t.isNotBlank() } }
                .take(3)
                .takeIf { it.isNotEmpty() }
        }.getOrNull()

        val relatedWords = runCatching {
            wordnikApi.getRelatedWords(wordText, apiKey)
        }.getOrNull()

        val synonyms = relatedWords
            ?.firstOrNull { it.relationshipType == "synonym" }
            ?.words?.take(10)
            ?.takeIf { it.isNotEmpty() }

        val antonyms = relatedWords
            ?.firstOrNull { it.relationshipType == "antonym" }
            ?.words?.take(10)
            ?.takeIf { it.isNotEmpty() }

        queueDao.updateDetails(
            id = queueId,
            ipa = ipa,
            examples = examples?.let { Json.encodeToString(it) },
            synonyms = synonyms?.let { Json.encodeToString(it) },
            antonyms = antonyms?.let { Json.encodeToString(it) }
        )
    }

    private fun WordQueueEntity.toDomain() = Word(
        id = id,
        word = word,
        definition = definition,
        ipa = ipa,
        examples = examples?.let { Json.decodeFromString<List<String>>(it) },
        synonyms = synonyms?.let { Json.decodeFromString<List<String>>(it) },
        antonyms = antonyms?.let { Json.decodeFromString<List<String>>(it) }
    )

    private fun String.stripHtml(): String =
        replace(Regex("<[^>]+>"), "").trim()
}
