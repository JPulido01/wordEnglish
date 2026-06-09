package com.example.wordenglish.worker

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wordenglish.widget.WordWidget
import dagger.hilt.android.EntryPointAccessors

class DailyWordWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            DailyWordWorkerEntryPoint::class.java
        )
        entryPoint.advanceQueueUseCase()()
        entryPoint.preCacheNextWordsUseCase()()
        entryPoint.getCurrentWordUseCase()()?.let {
            entryPoint.addToHistoryUseCase()(it)
            entryPoint.notificationSender().sendIfEnabled(it)
        }
        WordWidget().updateAll(applicationContext)
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "daily_word_update"
    }
}
