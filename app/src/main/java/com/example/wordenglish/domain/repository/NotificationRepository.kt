package com.example.wordenglish.domain.repository

import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    val isEnabled: Flow<Boolean>
    suspend fun setEnabled(enabled: Boolean)
}
