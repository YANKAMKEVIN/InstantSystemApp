package com.kev.instantsystem.ui

import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import com.kev.instantsystem.ui.home.HomeViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Plain JVM test (no Robolectric): the device language must reach the search. */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelLanguageTest {

    private val useCase = mockk<SearchEverythingUseCase>()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        every {
            useCase(query = any(), sortBy = any(), from = any(), to = any(), language = any())
        } returns flowOf(PagingData.empty<Article>())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setLanguage restarts the search with the new language`() = runTest {
        val viewModel = HomeViewModel(useCase)
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.discoveryArticles.collect {}
        }

        viewModel.setLanguage("en")
        advanceUntilIdle()

        coVerify { useCase(query = "technology", sortBy = any(), from = any(), to = any(), language = "en") }
    }
}
