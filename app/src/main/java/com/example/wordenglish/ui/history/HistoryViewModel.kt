package com.example.wordenglish.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {

    private val _history = MutableStateFlow<List<Word>>(emptyList())
    val history: StateFlow<List<Word>> = _history

    init {
        viewModelScope.launch {
            _history.value = getHistoryUseCase()
        }
    }
}
