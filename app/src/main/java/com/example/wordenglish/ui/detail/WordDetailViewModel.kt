package com.example.wordenglish.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.usecase.GetWordDetailUseCase
import com.example.wordenglish.domain.usecase.GetWordOfTheDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val word: Word) : DetailUiState
    data class Error(val message: String) : DetailUiState
}

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val getWordOfTheDayUseCase: GetWordOfTheDayUseCase,
    private val getWordDetailUseCase: GetWordDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    init {
        loadWordDetail()
    }

    private fun loadWordDetail() {
        viewModelScope.launch {
            val word = getWordOfTheDayUseCase()
            if (word == null) {
                _uiState.value = DetailUiState.Error("No word available")
                return@launch
            }
            _uiState.value = DetailUiState.Success(word)
            val enriched = getWordDetailUseCase(word.id)
            if (enriched != null) {
                _uiState.value = DetailUiState.Success(enriched)
            }
        }
    }
}
