package com.example.wordenglish.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.usecase.GetFavoritesUseCase
import com.example.wordenglish.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<Word>>(emptyList())
    val favorites: StateFlow<List<Word>> = _favorites

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = getFavoritesUseCase()
        }
    }

    fun removeFavorite(word: Word) {
        viewModelScope.launch {
            toggleFavoriteUseCase(word)
            loadFavorites()
        }
    }
}
