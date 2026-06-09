package com.example.wordenglish.data.repository

import android.content.Context
import com.example.wordenglish.data.local.WordDao
import com.example.wordenglish.data.local.WordEntity
import com.example.wordenglish.data.local.seed.WordSeedItem
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.WordRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepositoryImpl @Inject constructor(
    private val dao: WordDao,
    @ApplicationContext private val context: Context
) : WordRepository {

    private val seedMutex = Mutex()
    private var seeded = false

    override suspend fun getWordByIndex(index: Int): Word? {
        ensureSeeded()
        return dao.getWordByIndex(index)?.toDomain()
    }

    override suspend fun getWordById(id: Int): Word? {
        ensureSeeded()
        return dao.getWordById(id)?.toDomain()
    }

    override suspend fun getWordCount(): Int {
        ensureSeeded()
        return dao.getCount()
    }

    private suspend fun ensureSeeded() {
        if (seeded) return
        seedMutex.withLock {
            if (seeded) return
            if (dao.getCount() == 0) {
                dao.insertAll(loadWordsFromAssets())
            }
            seeded = true
        }
    }

    private suspend fun loadWordsFromAssets(): List<WordEntity> = withContext(Dispatchers.IO) {
        val json = context.assets.open("words.json").bufferedReader().readText()
        Json.decodeFromString<List<WordSeedItem>>(json).map {
            WordEntity(id = it.id, word = it.word, definition = it.definition)
        }
    }

    private fun WordEntity.toDomain() = Word(
        id = id,
        word = word,
        definition = definition,
        ipa = ipa,
        examples = examples?.let { Json.decodeFromString<List<String>>(it) },
        synonyms = synonyms?.let { Json.decodeFromString<List<String>>(it) },
        antonyms = antonyms?.let { Json.decodeFromString<List<String>>(it) }
    )
}
