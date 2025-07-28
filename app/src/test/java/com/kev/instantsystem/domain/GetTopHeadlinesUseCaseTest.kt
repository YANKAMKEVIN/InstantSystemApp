package com.kev.instantsystem.domain

import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.domain.repository.NewsRepository
import com.kev.instantsystem.domain.usecase.GetTopHeadlinesUseCase
import com.kev.instantsystem.testutils.collectPagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class GetTopHeadlinesUseCaseTest {

    private lateinit var repository: NewsRepository
    private lateinit var useCase: GetTopHeadlinesUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = object : NewsRepository {
            override fun getTopHeadlines(
                country: String,
                category: String?,
                query: String?
            ) = flowOf(
                PagingData.from(
                    listOf(
                        Article(
                            title = "Top Headline News",
                            description = "desc",
                            content = "content",
                            url = "https://headline.com",
                            publishedAt = "2025-07-24",
                            source = Source(name = "HeadSource", id = "hs1"),
                            author = "Editor",
                            urlToImage = "https://headline.com/img.jpg"
                        )
                    )
                )
            )

            override fun searchEverything(
                query: String,
                sortBy: String?,
                from: String?,
                to: String?,
                language: String?
            ) = throw NotImplementedError()
        }

        useCase = GetTopHeadlinesUseCase(repository)
    }

    @Test
    fun `invoke returns expected PagingData`() = runTest {
        val result = useCase(country = "fr", category = "technology", query = "android")
        val items = collectPagingData(result, testDispatcher, this)

        assertEquals(1, items.size)
        assertEquals("Top Headline News", items[0].title)
    }
}
