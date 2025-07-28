package com.kev.instantsystem.ui

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import com.kev.instantsystem.testutils.DummyDiffCallback
import com.kev.instantsystem.testutils.DummyListUpdateCallback
import com.kev.instantsystem.ui.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class HomeViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val useCase = mockk<SearchEverythingUseCase>()
    private lateinit var viewModel: HomeViewModel

    private val fakeArticle = Article(
        title = "Tech news 2025",
        description = "Innovations in AI",
        content = "Lots of AI breakthroughs",
        url = "https://tech.com/article",
        publishedAt = "2025-07-24",
        source = Source(name = "Tech Source", id = "tech-id"),
        author = "Tech Author",
        urlToImage = "https://tech.com/image.jpg"
    )

    @Before
    fun setup() {
        coEvery {
            useCase(query = "technology", language = "fr", sortBy = any(), from = any(), to = any())
        } returns flowOf(PagingData.from(listOf(fakeArticle)))

        viewModel = HomeViewModel(useCase)
    }

    @Test
    fun `discoveryArticles emits expected article`() = runTest(dispatcher) {
        // Simule le changement de langue comme dans HomeRoute (LaunchedEffect)
        viewModel.setLanguage("fr")

        val result = viewModel.discoveryArticles.first()

        val differ = AsyncPagingDataDiffer(
            diffCallback = DummyDiffCallback(),
            updateCallback = DummyListUpdateCallback(),
            workerDispatcher = dispatcher,
            mainDispatcher = dispatcher
        )

        differ.submitData(result)
        advanceUntilIdle()

        val items = differ.snapshot().items
        assertEquals(1, items.size)
        assertEquals("Tech news 2025", items[0].title)
    }
}

