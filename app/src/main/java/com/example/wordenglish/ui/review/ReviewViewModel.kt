package com.example.wordenglish.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.usecase.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _words = MutableStateFlow<List<Word>>(emptyList())
    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex

    val currentWord: Word? get() = _words.value.getOrNull(_currentIndex.value)
    val totalWords: Int get() = _words.value.size
    val isFinished: Boolean get() = _currentIndex.value >= _words.value.size

    init {
        viewModelScope.launch {
            _words.value = getFavoritesUseCase()
        }
    }

    fun next() {
        if (_currentIndex.value < _words.value.size) {
            _currentIndex.value++
        }
    }

    fun previous() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
        }
    }

    fun restart() {
        _currentIndex.value = 0
    }
}
