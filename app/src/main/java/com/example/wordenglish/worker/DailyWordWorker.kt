package com.example.wordenglish.worker

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.wordenglish.widget.WordWidget

class DailyWordWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        WordWidget().updateAll(applicationContext)
        return Result.success()
    }

    companion object {
        const val WORK_NAME = "daily_word_update"
    }
}
