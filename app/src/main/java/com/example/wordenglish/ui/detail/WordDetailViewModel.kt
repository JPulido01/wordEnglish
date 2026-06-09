package com.example.wordenglish.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.usecase.GetCurrentWordUseCase
import com.example.wordenglish.domain.usecase.IsFavoriteUseCase
import com.example.wordenglish.domain.usecase.ToggleFavoriteUseCase
import com.example.wordenglish.domain.repository.FavoriteRepository
import com.example.wordenglish.domain.repository.HistoryRepository
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
    savedStateHandle: SavedStateHandle,
    private val getCurrentWordUseCase: GetCurrentWordUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val favoriteRepository: FavoriteRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val favoriteId: Int? = savedStateHandle.get<Int>("favoriteId")
    private val historyId: Int? = savedStateHandle.get<Int>("historyId")

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    init {
        loadWord()
    }

    private fun loadWord() {
        viewModelScope.launch {
            val word = when {
                historyId != null -> historyRepository.getById(historyId)
                favoriteId != null -> favoriteRepository.getById(favoriteId)
                else -> getCurrentWordUseCase()
            }
            if (word == null) {
                _uiState.value = DetailUiState.Error("No word available")
                return@launch
            }
            _uiState.value = DetailUiState.Success(word)
            _isFavorite.value = isFavoriteUseCase(word.word)
        }
    }

    fun toggleFavorite() {
        val state = _uiState.value as? DetailUiState.Success ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(state.word)
            _isFavorite.value = isFavoriteUseCase(state.word.word)
        }
    }
}
