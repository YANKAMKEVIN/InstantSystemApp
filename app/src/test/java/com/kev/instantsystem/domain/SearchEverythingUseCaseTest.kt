package com.kev.instantsystem.domain

import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.domain.repository.NewsRepository
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
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
class SearchEverythingUseCaseTest {

    private lateinit var repository: NewsRepository
    private lateinit var useCase: SearchEverythingUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = object : NewsRepository {
            override fun getTopHeadlines(
                country: String, category: String?, query: String?
            ) = throw NotImplementedError()

            override fun searchEverything(
                query: String,
                sortBy: String?,
                from: String?,
                to: String?,
                language: String?
            ) = flowOf(
                PagingData.from(
                    listOf(
                        Article(
                            title = "Test News",
                            description = "desc",
                            content = "content",
                            url = "https://example.com",
                            publishedAt = "2025-07-24",
                            source = Source(name = "MockSource", id = "1"),
                            author = "Author",
                            urlToImage = "https://image.com"
                        )
                    )
                )
            )
        }
        useCase = SearchEverythingUseCase(repository)
    }

    @Test
    fun `invoke returns expected PagingData`() = runTest {
        val result = useCase(query = "bitcoin")
        val items = collectPagingData(result, testDispatcher, this)

        assertEquals(1, items.size)
        assertEquals("Test News", items[0].title)
    }
}
