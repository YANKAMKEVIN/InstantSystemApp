package com.kev.instantsystem.ui

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.domain.usecase.GetTopHeadlinesUseCase
import com.kev.instantsystem.testutils.DummyDiffCallback
import com.kev.instantsystem.testutils.DummyListUpdateCallback
import com.kev.instantsystem.ui.headlines.ArticleCategory
import com.kev.instantsystem.ui.headlines.HeadlinesViewModel
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
class HeadlinesViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val useCase = mockk<GetTopHeadlinesUseCase>()
    private lateinit var viewModel: HeadlinesViewModel

    private val mockArticle = Article(
        title = "Breaking News",
        description = "Some major event",
        content = "Full details here",
        url = "https://breaking.com",
        publishedAt = "2025-07-24",
        source = Source(name = "Breaking Source", id = "breaking-id"),
        author = "John Doe",
        urlToImage = "https://breaking.com/img.jpg"
    )

    @Before
    fun setup() {
        val pagingData = PagingData.from(listOf(mockArticle))

        val categories = listOf(
            ArticleCategory.Latest,
            ArticleCategory.Science,
            ArticleCategory.Sports,
            ArticleCategory.Technology,
            ArticleCategory.Health,
            ArticleCategory.Business
        )

        categories.forEach { category ->
            coEvery {
                useCase(country = "us", category = category.category, query = null)
            } returns flowOf(pagingData)
        }

        viewModel = HeadlinesViewModel(useCase)
    }


    @Test
    fun `articlesMap returns expected data for each category`() = runTest(dispatcher) {
        viewModel.articlesMap.forEach { (category, flow) ->
            val result = flow.first()

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
            assertEquals("Breaking News", items[0].title)
        }
    }
}
