package com.kev.instantsystem.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kev.instantsystem.data.paging.EverythingPagingSourceFactory
import com.kev.instantsystem.data.paging.NewsPagingSourceFactory
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source

class FakePagingSource : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val data = listOf(
            Article(
                title = "Fake News",
                description = "This is fake",
                content = "More details",
                url = "https://example.com",
                publishedAt = "2025-07-23",
                source = Source(
                    name = "Fake Source",
                    id = "fake-id"
                ),
                author = "Fake Author",
                urlToImage = "https://example.com/image.jpg"
            )
        )
        return LoadResult.Page(data, null, null)
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? = null
}

class FakeNewsPagingSourceFactory : NewsPagingSourceFactory(
    remoteDataSource = FakeRemoteDataSource()
) {
    override fun create(
        country: String,
        category: String?,
        query: String?
    ): PagingSource<Int, Article> = FakePagingSource()
}

class FakeEverythingPagingSourceFactory : EverythingPagingSourceFactory(
    dataSource = FakeRemoteDataSource()
) {
    override fun create(
        query: String,
        sortBy: String?,
        from: String?,
        to: String?,
        language: String?
    ): PagingSource<Int, Article> = FakePagingSource()
}

class FakeRemoteDataSource :
    NewsRemoteDataSource {
    override suspend fun getTopHeadlines(
        country: String?,
        category: String?,
        query: String?,
        page: Int,
        pageSize: Int
    ) = throw NotImplementedError()

    override suspend fun getEverything(
        query: String,
        sortBy: String?,
        from: String?,
        to: String?,
        language: String?,
        page: Int,
        pageSize: Int
    ) = throw NotImplementedError()
}