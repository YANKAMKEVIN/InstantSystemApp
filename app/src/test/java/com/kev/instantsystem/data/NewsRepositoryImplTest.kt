package com.kev.instantsystem.data

import com.kev.instantsystem.data.paging.EverythingPagingSourceFactory
import com.kev.instantsystem.data.paging.NewsPagingSourceFactory
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.data.repository.NewsRepositoryImpl
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** Plain JVM test (no Robolectric) of the date of the cached top headlines. */
class NewsRepositoryImplTest {

    private val dao = FakeHeadlinesDao()
    private val repository = NewsRepositoryImpl(
        headlinesPagingSourceFactory = mockk<NewsPagingSourceFactory>(),
        everythingPagingSourceFactory = mockk<EverythingPagingSourceFactory>(),
        remoteDataSource = mockk<NewsRemoteDataSource>(),
        headlinesDao = dao
    )

    private val article = Article(
        source = Source(id = null, name = "Source"),
        author = null,
        title = "Title",
        description = null,
        url = "https://a",
        urlToImage = null,
        publishedAt = "2025-07-24T12:00:00Z",
        content = null
    )

    @Test
    fun `the date of the cached list is that of its country and category`() = runTest {
        assertNull(repository.headlinesUpdatedAt("us", "sports").first())

        dao.replaceList("us:sports", listOf(article), nextPage = 2, updatedAt = 42L)

        assertEquals(42L, repository.headlinesUpdatedAt("us", "sports").first())
        assertNull(repository.headlinesUpdatedAt("us", null).first())
    }
}
