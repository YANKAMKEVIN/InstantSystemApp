package com.kev.instantsystem.ui

import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import com.kev.instantsystem.testutils.DummyDiffCallback
import com.kev.instantsystem.testutils.DummyListUpdateCallback
import com.kev.instantsystem.ui.search.SearchViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private val dispatcher = StandardTestDispatcher()
    private val useCase = mockk<SearchEverythingUseCase>()

    private val fakeArticle = Article(
        title = "Bitcoin explodes",
        description = "Market update",
        content = "Full content",
        url = "https://news.com/article",
        publishedAt = "2025-07-24",
        source = Source(name = "Mock Source", id = "mock-id"),
        author = "Mock Author",
        urlToImage = "https://news.com/image.jpg"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        coEvery { useCase.invoke("bitcoin", any(), any(), any(), any()) } returns flowOf(
            PagingData.from(listOf(fakeArticle))
        )
        viewModel = SearchViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `searchResults emits results after query is updated`() = runTest(dispatcher) {
        viewModel.onQueryChanged("bitcoin")

        val result = viewModel.searchResults.first()

        val differ = androidx.paging.AsyncPagingDataDiffer(
            diffCallback = DummyDiffCallback(),
            updateCallback = DummyListUpdateCallback(),
            workerDispatcher = dispatcher,
            mainDispatcher = dispatcher
        )

        differ.submitData(result)

        advanceUntilIdle()

        val items = differ.snapshot().items
        assertEquals(1, items.size)
        assertEquals("Bitcoin explodes", items[0].title)
    }
}
