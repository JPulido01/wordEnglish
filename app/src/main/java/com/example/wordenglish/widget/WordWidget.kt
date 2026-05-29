package com.example.wordenglish.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.GlanceTheme
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.wordenglish.domain.model.Word
import dagger.hilt.android.EntryPointAccessors

class WordWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val useCase = EntryPointAccessors
            .fromApplication(context.applicationContext, WordWidgetEntryPoint::class.java)
            .getWordOfTheDayUseCase()

        val word = runCatching { useCase() }.getOrNull()

        provideContent {
            GlanceTheme {
                WordWidgetContent(word)
            }
        }
    }
}

@Composable
private fun WordWidgetContent(word: Word?) {
    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.surface)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (word == null) {
            Text(
                text = "No word available",
                style = TextStyle(color = GlanceTheme.colors.onSurface)
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = word.word,
                    style = TextStyle(
                        color = GlanceTheme.colors.primary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 1
                )
                Spacer(modifier = GlanceModifier.height(8.dp))
                Text(
                    text = word.definition,
                    style = TextStyle(
                        color = GlanceTheme.colors.onSurface,
                        fontSize = 13.sp
                    ),
                    maxLines = 4
                )
            }
        }
    }
}
