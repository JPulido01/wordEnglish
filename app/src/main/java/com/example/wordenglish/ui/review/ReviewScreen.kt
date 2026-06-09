package com.example.wordenglish.ui.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val index by viewModel.currentIndex.collectAsState()
    val word = viewModel.currentWord
    val total = viewModel.totalWords
    val finished = viewModel.isFinished

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (!finished && total > 0) {
                        Text("Repaso  ${index + 1} / $total")
                    } else {
                        Text("Repaso")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when {
            total == 0 -> Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay palabras para repasar",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            finished -> FinishedContent(
                modifier = Modifier.padding(padding),
                onRestart = viewModel::restart,
                onBack = onNavigateBack
            )

            word != null -> ReviewContent(
                word = word,
                index = index,
                total = total,
                onPrevious = viewModel::previous,
                onNext = viewModel::next,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ReviewContent(
    word: com.example.wordenglish.domain.model.Word,
    index: Int,
    total: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = word.word,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            if (!word.ipa.isNullOrBlank()) {
                Text(
                    text = word.ipa,
                    style = MaterialTheme.typography.bodyLarge,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = word.definition, style = MaterialTheme.typography.bodyLarge)

            if (!word.examples.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Examples", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                word.examples.forEach { ex ->
                    Text(
                        text = "“$ex”",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            if (!word.synonyms.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Synonyms", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    word.synonyms.forEach { SuggestionChip(onClick = {}, label = { Text(it) }) }
                }
            }
            if (!word.antonyms.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text("Antonyms", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    word.antonyms.forEach { SuggestionChip(onClick = {}, label = { Text(it) }) }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FilledTonalButton(onClick = onPrevious, enabled = index > 0) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
                Text("  Anterior")
            }
            FilledTonalButton(onClick = onNext) {
                Text("Siguiente  ")
                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null)
            }
        }
    }
}

@Composable
private fun FinishedContent(
    modifier: Modifier = Modifier,
    onRestart: () -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Text("¡Repaso completado!", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Has repasado todas tus palabras favoritas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onRestart) {
                Icon(Icons.Rounded.Refresh, contentDescription = null)
                Text("  Repetir repaso")
            }
            Spacer(modifier = Modifier.height(12.dp))
            FilledTonalButton(onClick = onBack) { Text("Volver a favoritos") }
        }
    }
}

@Composable
private fun FilledTonalButton(onClick: () -> Unit, enabled: Boolean = true, content: @Composable () -> Unit) {
    androidx.compose.material3.FilledTonalButton(onClick = onClick, enabled = enabled) { content() }
}
