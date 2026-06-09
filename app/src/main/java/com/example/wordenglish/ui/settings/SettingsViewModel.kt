package com.example.wordenglish.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wordenglish.domain.model.WordInterval
import com.example.wordenglish.domain.repository.IntervalRepository
import com.example.wordenglish.domain.repository.NotificationRepository
import androidx.glance.appwidget.updateAll
import com.example.wordenglish.widget.WordWidget
import com.example.wordenglish.worker.DailyWordWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val intervalRepository: IntervalRepository,
    private val notificationRepository: NotificationRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val selectedInterval = intervalRepository.interval.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    val notificationsEnabled = notificationRepository.isEnabled.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true
    )

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch { notificationRepository.setEnabled(enabled) }
    }

    fun setInterval(interval: WordInterval) {
        viewModelScope.launch {
            intervalRepository.setInterval(interval)
            rescheduleWork(interval)
            WordWidget().updateAll(context)
        }
    }

    private fun rescheduleWork(interval: WordInterval) {
        val work = PeriodicWorkRequestBuilder<DailyWordWorker>(
            interval.hours.toLong(), TimeUnit.HOURS
        ).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            DailyWordWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            work
        )
    }
}
