package com.example.wordenglish.notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wordenglish.R
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.NotificationRepository
import com.example.wordenglish.ui.detail.WordDetailActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

const val CHANNEL_ID = "word_of_the_day"
private const val NOTIFICATION_ID = 1001

@Singleton
class NotificationSender @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationRepository: NotificationRepository
) {
    suspend fun sendIfEnabled(word: Word) {
        if (!notificationRepository.isEnabled.first()) return

        val intent = Intent(context, WordDetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(word.word)
            .setContentText(word.definition)
            .setStyle(NotificationCompat.BigTextStyle().bigText(word.definition))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
}
