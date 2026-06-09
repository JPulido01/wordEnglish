package com.example.wordenglish

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wordenglish.domain.repository.HistoryRepository
import com.example.wordenglish.domain.repository.IntervalRepository
import com.example.wordenglish.domain.repository.WordQueueRepository
import androidx.glance.appwidget.updateAll
import com.example.wordenglish.notifications.CHANNEL_ID
import com.example.wordenglish.widget.WordWidget
import com.example.wordenglish.worker.DailyWordWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class WordEnglishApp : Application() {

    @Inject lateinit var intervalRepository: IntervalRepository
    @Inject lateinit var wordQueueRepository: WordQueueRepository
    @Inject lateinit var historyRepository: HistoryRepository

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        MainScope().launch {
            wordQueueRepository.initializeIfEmpty()
            wordQueueRepository.getCurrentWord()?.let { historyRepository.add(it) }
            WordWidget().updateAll(applicationContext)
        }
        scheduleWidgetUpdate()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, "Palabra del día", NotificationManager.IMPORTANCE_DEFAULT)
            .apply { description = "Nueva palabra cada día" }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun scheduleWidgetUpdate() {
        val intervalHours = runBlocking {
            intervalRepository.interval.first()?.hours?.toLong() ?: 24L
        }
        val work = PeriodicWorkRequestBuilder<DailyWordWorker>(intervalHours, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DailyWordWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            work
        )
    }
}
