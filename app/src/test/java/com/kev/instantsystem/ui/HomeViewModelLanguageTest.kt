package com.kev.instantsystem.ui

import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import com.kev.instantsystem.ui.home.HomeViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Locale

/** Plain JVM test (no Robolectric): the device language must reach the search. */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelLanguageTest {

    private val useCase = mockk<SearchEverythingUseCase>()
    private val deviceLocale = Locale.getDefault()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        Locale.setDefault(Locale.FRANCE)
        every {
            useCase(query = any(), sortBy = any(), from = any(), to = any(), language = any())
        } returns flowOf(PagingData.empty<Article>())
    }

    @After
    fun tearDown() {
        Locale.setDefault(deviceLocale)
        Dispatchers.resetMain()
    }

    private fun TestScope.collect(viewModel: HomeViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.discoveryArticles.collect {}
        }
    }

    private fun verifySearch(language: String, times: Int = 1) = verify(exactly = times) {
        useCase(query = "technology", sortBy = any(), from = any(), to = any(), language = language)
    }

    @Test
    fun `setLanguage restarts the search with the new language`() = runTest {
        val viewModel = HomeViewModel(useCase)
        collect(viewModel)

        viewModel.setLanguage("en")
        advanceUntilIdle()

        verifySearch("en")
    }

    @Test
    fun `the first search uses the device language and is not repeated for it`() = runTest {
        Locale.setDefault(Locale.GERMANY)
        val viewModel = HomeViewModel(useCase)
        collect(viewModel)

        viewModel.setLanguage("de")
        advanceUntilIdle()

        verifySearch("de", times = 1)
        verifySearch("fr", times = 0)
    }

    @Test
    fun `a device language NewsAPI does not support searches in English`() = runTest {
        Locale.setDefault(Locale.JAPAN)
        val viewModel = HomeViewModel(useCase)
        collect(viewModel)
        advanceUntilIdle()

        verifySearch("en")
    }
}
