package com.example.wordenglish.ui.settings

import android.content.Context
import com.example.wordenglish.domain.repository.IntervalRepository
import com.example.wordenglish.domain.repository.NotificationRepository
import com.example.wordenglish.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val intervalRepository = mockk<IntervalRepository>()
    private val notificationRepository = mockk<NotificationRepository>()
    private val context = mockk<Context>(relaxed = true)

    private fun createViewModel(): SettingsViewModel {
        every { intervalRepository.interval } returns flowOf(null)
        every { notificationRepository.isEnabled } returns flowOf(true)
        return SettingsViewModel(intervalRepository, notificationRepository, context)
    }

    @Test
    fun `setNotificationsEnabled persists disabled`() = runTest {
        coEvery { notificationRepository.setEnabled(false) } just Runs

        val vm = createViewModel()
        vm.setNotificationsEnabled(false)
        advanceUntilIdle()

        coVerify(exactly = 1) { notificationRepository.setEnabled(false) }
    }

    @Test
    fun `setNotificationsEnabled persists enabled`() = runTest {
        coEvery { notificationRepository.setEnabled(true) } just Runs

        val vm = createViewModel()
        vm.setNotificationsEnabled(true)
        advanceUntilIdle()

        coVerify(exactly = 1) { notificationRepository.setEnabled(true) }
    }
}
