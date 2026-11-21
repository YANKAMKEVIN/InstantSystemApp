package com.kev.instantsystem.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import com.kev.instantsystem.data.dto.ArticleDto
import com.kev.instantsystem.data.dto.NewsDto
import com.kev.instantsystem.data.dto.SourceDto
import com.kev.instantsystem.data.local.HeadlineArticleEntity
import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkException
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.paging.HeadlinesRemoteMediator
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Plain JVM test (no Robolectric) of the offline cache of the top headlines. */
@OptIn(ExperimentalPagingApi::class)
class HeadlinesRemoteMediatorTest {

    private val remote = mockk<NewsRemoteDataSource>()
    private val dao = FakeHeadlinesDao()
    private var clock = 1_000L
    private val mediator = HeadlinesRemoteMediator(remote, dao, country = "us", category = "sports", now = { clock })
    private val state = PagingState<Int, HeadlineArticleEntity>(
        pages = emptyList(),
        anchorPosition = null,
        config = PagingConfig(pageSize = 20),
        leadingPlaceholderCount = 0
    )

    private fun dto(title: String) = ArticleDto(
        source = SourceDto(id = "s", name = "Source"),
        author = null,
        title = title,
        description = null,
        url = "https://news/$title",
        urlToImage = null,
        publishedAt = "2025-07-24T12:00:00Z",
        content = null
    )

    private fun article(title: String) = Article(
        source = Source(id = "s", name = "Source"),
        author = null,
        title = title,
        description = null,
        url = "https://news/$title",
        urlToImage = null,
        publishedAt = "2025-07-24T12:00:00Z",
        content = null
    )

    private fun answerPage(page: Int, vararg titles: String) {
        coEvery {
            remote.getTopHeadlines(country = "us", category = "sports", query = null, page = page, pageSize = 20)
        } returns NetworkResponse.Success(NewsDto("ok", titles.size, titles.map(::dto)))
    }

    private fun failPage(page: Int) {
        coEvery {
            remote.getTopHeadlines(country = "us", category = "sports", query = null, page = page, pageSize = 20)
        } returns NetworkResponse.Failure(NetworkError.UnResolveAddress("offline"))
    }

    private fun cachedTitles() = dao.articlesOf("us:sports").map { it.title }

    @Test
    fun `a refresh replaces the cached list and records its date`() = runTest {
        dao.replaceList("us:sports", listOf(article("old")), nextPage = 2, updatedAt = 1L)
        answerPage(1, "a", "b")

        val result = mediator.load(LoadType.REFRESH, state)

        assertEquals(false, (result as MediatorResult.Success).endOfPaginationReached)
        assertEquals(listOf("a", "b"), cachedTitles())
        assertEquals(listOf(0, 1), dao.articlesOf("us:sports").map { it.position })
        assertEquals(1_000L, dao.updatedAt("us:sports").first())
        assertEquals(2, dao.list("us:sports")?.nextPage)
    }

    @Test
    fun `offline, the refresh fails and the last list received is kept`() = runTest {
        dao.replaceList("us:sports", listOf(article("cached")), nextPage = 2, updatedAt = 500L)
        failPage(1)

        val result = mediator.load(LoadType.REFRESH, state)

        val error = (result as MediatorResult.Error).throwable
        assertTrue(error is NetworkException && error.error is NetworkError.UnResolveAddress)
        assertEquals(listOf("cached"), cachedTitles())
        assertEquals(500L, dao.updatedAt("us:sports").first())
    }

    @Test
    fun `a next page goes after the cached articles and keeps the list date`() = runTest {
        answerPage(1, "a", "b")
        mediator.load(LoadType.REFRESH, state)
        clock = 2_000L
        answerPage(2, "c")

        val result = mediator.load(LoadType.APPEND, state)

        assertEquals(false, (result as MediatorResult.Success).endOfPaginationReached)
        assertEquals(listOf("a", "b", "c"), cachedTitles())
        assertEquals(listOf(0, 1, 2), dao.articlesOf("us:sports").map { it.position })
        assertEquals(1_000L, dao.updatedAt("us:sports").first())
        assertEquals(3, dao.list("us:sports")?.nextPage)
    }

    @Test
    fun `an empty page ends the list and no further page is requested`() = runTest {
        answerPage(1, "a")
        mediator.load(LoadType.REFRESH, state)
        answerPage(2)

        val end = mediator.load(LoadType.APPEND, state)
        val after = mediator.load(LoadType.APPEND, state)

        assertTrue((end as MediatorResult.Success).endOfPaginationReached)
        assertTrue((after as MediatorResult.Success).endOfPaginationReached)
        assertNull(dao.list("us:sports")?.nextPage)
        coVerify(exactly = 0) { remote.getTopHeadlines(any(), any(), any(), page = 3, pageSize = any()) }
    }

    @Test
    fun `nothing is loaded before the list`() = runTest {
        val prepend = mediator.load(LoadType.PREPEND, state)
        val appendWithoutCache = mediator.load(LoadType.APPEND, state)

        assertTrue((prepend as MediatorResult.Success).endOfPaginationReached)
        assertTrue((appendWithoutCache as MediatorResult.Success).endOfPaginationReached)
        coVerify(exactly = 0) { remote.getTopHeadlines(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `each category has its own cache`() = runTest {
        answerPage(1, "sport")
        mediator.load(LoadType.REFRESH, state)
        coEvery {
            remote.getTopHeadlines(country = "us", category = null, query = null, page = 1, pageSize = 20)
        } returns NetworkResponse.Success(NewsDto("ok", 1, listOf(dto("latest"))))

        HeadlinesRemoteMediator(remote, dao, country = "us", category = null, now = { 3_000L })
            .load(LoadType.REFRESH, state)

        assertEquals(listOf("sport"), cachedTitles())
        assertEquals(listOf("latest"), dao.articlesOf("us:").map { it.title })
        assertEquals(1_000L, dao.updatedAt("us:sports").first())
        assertEquals(3_000L, dao.updatedAt("us:").first())
    }
}
