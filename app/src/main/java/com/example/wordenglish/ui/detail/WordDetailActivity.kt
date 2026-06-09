package com.example.wordenglish.ui.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.wordenglish.ui.theme.WordEnglishTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordEnglishTheme {
                WordDetailScreen(onNavigateBack = { finish() })
            }
        }
    }
}
