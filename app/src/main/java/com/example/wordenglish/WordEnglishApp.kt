package com.example.wordenglish

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.wordenglish.domain.repository.IntervalRepository
import com.example.wordenglish.worker.DailyWordWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class WordEnglishApp : Application() {

    @Inject lateinit var intervalRepository: IntervalRepository

    override fun onCreate() {
        super.onCreate()
        scheduleWidgetUpdate()
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
