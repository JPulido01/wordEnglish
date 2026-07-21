package com.example.wordenglish.ui.detail

import androidx.lifecycle.SavedStateHandle
import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.FavoriteRepository
import com.example.wordenglish.domain.repository.HistoryRepository
import com.example.wordenglish.domain.usecase.GetCurrentWordDetailUseCase
import com.example.wordenglish.domain.usecase.IsFavoriteUseCase
import com.example.wordenglish.domain.usecase.ToggleFavoriteUseCase
import com.example.wordenglish.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WordDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCurrentWordDetailUseCase = mockk<GetCurrentWordDetailUseCase>()
    private val isFavoriteUseCase = mockk<IsFavoriteUseCase>()
    private val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>()
    private val favoriteRepository = mockk<FavoriteRepository>()
    private val historyRepository = mockk<HistoryRepository>()

    private fun createViewModel(
        savedStateHandle: SavedStateHandle = SavedStateHandle()
    ) = WordDetailViewModel(
        savedStateHandle,
        getCurrentWordDetailUseCase,
        isFavoriteUseCase,
        toggleFavoriteUseCase,
        favoriteRepository,
        historyRepository
    )

    @Test
    fun `loads current word when no args`() = runTest {
        val word = Word(id = 1, word = "abandon", definition = "to leave behind")
        coEvery { getCurrentWordDetailUseCase() } returns word
        coEvery { isFavoriteUseCase("abandon") } returns false

        val vm = createViewModel()
        advanceUntilIdle()

        assertEquals(DetailUiState.Success(word), vm.uiState.value)
        assertFalse(vm.isFavorite.value)
    }

    @Test
    fun `loads favorite when favoriteId is provided`() = runTest {
        val word = Word(id = 2, word = "benign", definition = "gentle and kind")
        coEvery { favoriteRepository.getById(2) } returns word
        coEvery { isFavoriteUseCase("benign") } returns true

        val vm = createViewModel(SavedStateHandle(mapOf("favoriteId" to 2)))
        advanceUntilIdle()

        assertEquals(DetailUiState.Success(word), vm.uiState.value)
        assertTrue(vm.isFavorite.value)
    }

    @Test
    fun `shows error state when word is null`() = runTest {
        coEvery { getCurrentWordDetailUseCase() } returns null

        val vm = createViewModel()
        advanceUntilIdle()

        assertTrue(vm.uiState.value is DetailUiState.Error)
    }

    @Test
    fun `toggleFavorite refreshes isFavorite`() = runTest {
        val word = Word(id = 1, word = "abandon", definition = "to leave behind")
        coEvery { getCurrentWordDetailUseCase() } returns word
        coEvery { isFavoriteUseCase("abandon") } returnsMany listOf(false, true)
        coEvery { toggleFavoriteUseCase(word) } just Runs

        val vm = createViewModel()
        advanceUntilIdle()
        assertFalse(vm.isFavorite.value)

        vm.toggleFavorite()
        advanceUntilIdle()

        coVerify(exactly = 1) { toggleFavoriteUseCase(word) }
        assertTrue(vm.isFavorite.value)
    }
}
