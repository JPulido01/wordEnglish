package com.example.wordenglish.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.wordenglish.domain.model.WordInterval
import com.example.wordenglish.domain.repository.IntervalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IntervalRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : IntervalRepository {

    private val INTERVAL_KEY = intPreferencesKey("word_interval_hours")

    override val interval: Flow<WordInterval?> = dataStore.data.map { preferences ->
        val hours = preferences[INTERVAL_KEY] ?: return@map null
        WordInterval.entries.find { it.hours == hours }
    }

    override suspend fun setInterval(interval: WordInterval) {
        dataStore.edit { preferences ->
            preferences[INTERVAL_KEY] = interval.hours
        }
    }
}
