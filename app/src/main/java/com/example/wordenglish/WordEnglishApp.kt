package com.example.wordenglish

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wordenglish.worker.DailyWordWorker
import dagger.hilt.android.HiltAndroidApp
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class WordEnglishApp : Application() {

    override fun onCreate() {
        super.onCreate()
        scheduleDailyWidgetUpdate()
    }

    private fun scheduleDailyWidgetUpdate() {
        val now = LocalDateTime.now()
        val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()
        val initialDelay = ChronoUnit.MINUTES.between(now, nextMidnight)

        val dailyWork = PeriodicWorkRequestBuilder<DailyWordWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DailyWordWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWork
        )
    }
}
