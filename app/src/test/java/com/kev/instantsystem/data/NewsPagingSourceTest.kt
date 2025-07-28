package com.kev.instantsystem.data

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kev.instantsystem.data.dto.ArticleDto
import com.kev.instantsystem.data.dto.NewsDto
import com.kev.instantsystem.data.dto.SourceDto
import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.paging.NewsPagingSource
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NewsPagingSourceTest {

    private val fakeRemoteDataSource = object : NewsRemoteDataSource {
        override suspend fun getTopHeadlines(
            country: String?,
            category: String?,
            query: String?,
            page: Int,
            pageSize: Int
        ): NetworkResponse<NewsDto> {
            val articles = listOf(
                ArticleDto(
                    title = "Mocked News",
                    description = "Description",
                    content = "Content",
                    url = "https://example.com",
                    publishedAt = "2025-07-24",
                    source = SourceDto(
                        "mock-id",
                        "Mock Source"
                    ),
                    author = "Mock Author",
                    urlToImage = "https://example.com/image.jpg"
                )
            )
            return NetworkResponse.Success(
                NewsDto(
                    status = "ok",
                    totalResults = 1,
                    articles = articles
                )
            )
        }

        override suspend fun getEverything(
            query: String,
            sortBy: String?,
            from: String?,
            to: String?,
            language: String?,
            page: Int,
            pageSize: Int
        ): NetworkResponse<NewsDto> {
            throw NotImplementedError()
        }
    }

    @Test
    fun `load returns page when remote source succeeds`() = runTest {
        // Given
        val pagingSource = NewsPagingSource(
            remoteDataSource = fakeRemoteDataSource,
            country = "fr",
            category = null,
            query = null
        )

        // When
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // Then
        val expected = listOf(
            Article(
                title = "Mocked News",
                description = "Description",
                content = "Content",
                url = "https://example.com",
                publishedAt = "2025-07-24",
                source = Source(
                    id = "mock-id",
                    name = "Mock Source"
                ),
                author = "Mock Author",
                urlToImage = "https://example.com/image.jpg"
            )
        )

        assert(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page

        assertEquals(expected, result.data)
        assertEquals(null, result.prevKey)
        assertEquals(2, result.nextKey)
    }

    @Test
    fun `load returns error when remote source fails`() = runTest {
        // Fake data source to mock an error erreur
        val errorDataSource = object : NewsRemoteDataSource {
            override suspend fun getTopHeadlines(
                country: String?,
                category: String?,
                query: String?,
                page: Int,
                pageSize: Int
            ): NetworkResponse<NewsDto> {
                return NetworkResponse.Failure(
                    error = NetworkError.InternalServerError(
                        message = "Internal Server Error"
                    )
                )
            }

            override suspend fun getEverything(
                query: String,
                sortBy: String?,
                from: String?,
                to: String?,
                language: String?,
                page: Int,
                pageSize: Int
            ): NetworkResponse<NewsDto> {
                throw NotImplementedError()
            }
        }

        val pagingSource = NewsPagingSource(
            remoteDataSource = errorDataSource,
            country = "fr",
            category = null,
            query = null
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        // ✅ Assert : the result est error with the correct message
        assert(result is PagingSource.LoadResult.Error)
        result as PagingSource.LoadResult.Error

        assert(result.throwable is Exception)
        assert((result.throwable as Exception).message!!.contains("Internal Server Error"))
    }

    @Test
    fun `getRefreshKey returns correct key based on anchor position`() {
        val dummyArticle = Article(
            title = "Test",
            description = null,
            content = null,
            url = "https://example.com",
            publishedAt = "2025-07-24",
            source = Source("Test Source", "id"),
            author = null,
            urlToImage = null
        )

        val pagingSource = NewsPagingSource(
            remoteDataSource = FakeRemoteDataSource(),
            country = "fr",
            category = null,
            query = null
        )

        val state = PagingState(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = listOf(dummyArticle),
                    prevKey = 1,
                    nextKey = 3
                )
            ),
            anchorPosition = 0,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        val refreshKey = pagingSource.getRefreshKey(state)

        // then
        assertEquals(2, refreshKey)
    }


}
