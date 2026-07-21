package com.example.wordenglish.domain.usecase

import com.example.wordenglish.domain.model.Word
import com.example.wordenglish.domain.repository.WordQueueRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GetCurrentWordUseCaseTest {

    private val repository = mockk<WordQueueRepository>()
    private val useCase = GetCurrentWordUseCase(repository)

    @Test
    fun `returns word from repository`() = runTest {
        val word = Word(id = 1, word = "abandon", definition = "to leave behind")
        coEvery { repository.getCurrentWord() } returns word

        assertEquals(word, useCase())
    }

    @Test
    fun `returns null when repository is empty`() = runTest {
        coEvery { repository.getCurrentWord() } returns null

        assertNull(useCase())
    }
}
