package com.kev.instantsystem.data

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kev.instantsystem.data.dto.ArticleDto
import com.kev.instantsystem.data.dto.NewsDto
import com.kev.instantsystem.data.dto.SourceDto
import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.paging.EverythingPagingSource
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class EverythingPagingSourceTest {

    private val fakeRemoteDataSource = object : NewsRemoteDataSource {
        override suspend fun getTopHeadlines(
            country: String?, category: String?, query: String?, page: Int, pageSize: Int
        ) = throw NotImplementedError()

        override suspend fun getEverything(
            query: String,
            sortBy: String?,
            from: String?,
            to: String?,
            language: String?,
            page: Int,
            pageSize: Int
        ): NetworkResponse<NewsDto> {
            val articles = listOf(
                ArticleDto(
                    title = "Search Result",
                    description = "Found it",
                    content = "Search content",
                    url = "https://example.com/search",
                    publishedAt = "2025-07-24",
                    source = SourceDto(
                        id = "id",
                        name = "Search Source"
                    ),
                    author = "Search Author",
                    urlToImage = "https://example.com/image.jpg"
                )
            )
            return NetworkResponse.Success(NewsDto("ok", 1, articles))
        }
    }

    @Test
    fun `load returns page when getEverything succeeds`() = runTest {
        val pagingSource = EverythingPagingSource(
            dataSource = fakeRemoteDataSource,
            query = "android",
            sortBy = null,
            from = null,
            to = null,
            language = "fr"
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        val expected = listOf(
            Article(
                title = "Search Result",
                description = "Found it",
                content = "Search content",
                url = "https://example.com/search",
                publishedAt = "2025-07-24",
                source = Source(name = "Search Source", id = "id"),
                author = "Search Author",
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
    fun `load returns error when getEverything fails`() = runTest {
        val errorDataSource = object : NewsRemoteDataSource {
            override suspend fun getTopHeadlines(
                country: String?, category: String?, query: String?, page: Int, pageSize: Int
            ) = throw NotImplementedError()

            override suspend fun getEverything(
                query: String, sortBy: String?, from: String?, to: String?, language: String?,
                page: Int, pageSize: Int
            ): NetworkResponse<NewsDto> {
                return NetworkResponse.Failure(NetworkError.InternalServerError("Server down"))
            }
        }

        val pagingSource = EverythingPagingSource(
            dataSource = errorDataSource,
            query = "error",
            sortBy = null,
            from = null,
            to = null,
            language = "fr"
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Error)
        result as PagingSource.LoadResult.Error
        assert(result.throwable.message!!.contains("Server down"))
    }

    @Test
    fun `getRefreshKey returns correct key`() {
        val dummyArticle = Article(
            title = "Dummy",
            description = null,
            content = null,
            url = "https://example.com",
            publishedAt = "2025-07-24",
            source = Source("Dummy Source", "id"),
            author = null,
            urlToImage = null
        )

        val pagingSource = EverythingPagingSource(
            dataSource = NewsRepositoryImplTest.FakeRemoteDataSource(),
            query = "kotlin",
            sortBy = null,
            from = null,
            to = null,
            language = "fr"
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
        assertEquals(2, refreshKey)
    }

    @Test
    fun `load returns empty page when response has no articles`() = runTest {
        val emptyDataSource = object : NewsRemoteDataSource {
            override suspend fun getTopHeadlines(
                country: String?, category: String?, query: String?, page: Int, pageSize: Int
            ) = throw NotImplementedError()

            override suspend fun getEverything(
                query: String, sortBy: String?, from: String?, to: String?, language: String?,
                page: Int, pageSize: Int
            ): NetworkResponse<NewsDto> {
                return NetworkResponse.Success(
                    NewsDto(
                        status = "ok",
                        totalResults = 0,
                        articles = emptyList()
                    )
                )
            }
        }

        val pagingSource = EverythingPagingSource(
            dataSource = emptyDataSource,
            query = "empty",
            sortBy = null,
            from = null,
            to = null,
            language = "fr"
        )

        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Page)
        result as PagingSource.LoadResult.Page

        assert(result.data.isEmpty())
        assertEquals(null, result.prevKey)
        assertEquals(null, result.nextKey)
    }

}
