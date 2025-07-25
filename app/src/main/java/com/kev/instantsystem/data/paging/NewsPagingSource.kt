package com.kev.instantsystem.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kev.instantsystem.data.mapper.toDomain
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.ynfluence.ui.screens.news.ui.model.Article

/**
 * PagingSource for fetching paginated news results from the `/v2/top-headlines` endpoint.
 *
 * This class handles loading pages of articles based on search query and optional filters.
 * It wraps the network result using a RemoteDataSource that returns a [NetworkResponse],
 * and maps successful results to [Article] domain models.
 */
class NewsPagingSource(
    private val remoteDataSource: NewsRemoteDataSource,
    private val country: String?,
    private val category: String?,
    private val query: String?
) : PagingSource<Int, Article>() {

    companion object {
        private const val INITIAL_PAGE = 1
        private const val PAGE_SIZE = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: INITIAL_PAGE

        val result = remoteDataSource.getTopHeadlines(
            country = country,
            category = category,
            query = query,
            page = page,
            pageSize = PAGE_SIZE,
        )
        return when (result) {
            is NetworkResponse.Success -> {
                val articles = result.data.articles.map { it.toDomain() }
                LoadResult.Page(
                    data = articles,
                    prevKey = if (page == INITIAL_PAGE) null else page - 1,
                    nextKey = if (articles.isEmpty()) null else page + 1
                )
            }

            is NetworkResponse.Failure -> {
                LoadResult.Error(Exception(result.error.message))
            }
        }

    }


    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
