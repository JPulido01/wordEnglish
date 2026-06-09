package com.example.wordenglish.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.wordenglish.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : NotificationRepository {

    private val KEY = booleanPreferencesKey("notifications_enabled")

    override val isEnabled: Flow<Boolean> = dataStore.data.map { it[KEY] ?: true }

    override suspend fun setEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY] = enabled }
    }
}
