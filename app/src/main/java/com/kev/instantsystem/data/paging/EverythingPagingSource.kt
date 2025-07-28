package com.kev.instantsystem.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kev.instantsystem.data.mapper.toDomain
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.domain.model.Article

/**
 * PagingSource for fetching paginated news results from the `/v2/everything` endpoint.
 *
 * This class handles loading pages of articles based on search query and optional filters.
 * It wraps the network result using a RemoteDataSource that returns a [NetworkResponse],
 * and maps successful results to [Article] domain models.
 */
class EverythingPagingSource(
    private val dataSource: NewsRemoteDataSource,
    private val query: String,
    private val sortBy: String?,
    private val from: String?,
    private val to: String?,
    private val language: String?
) : PagingSource<Int, Article>() {

    companion object {
        private const val INITIAL_PAGE = 1
        private const val PAGE_SIZE = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: INITIAL_PAGE

        return when (val response = dataSource.getEverything(
            query = query,
            sortBy = sortBy,
            from = from,
            to = to,
            language = language,
            page = page,
            pageSize = PAGE_SIZE
        )) {
            is NetworkResponse.Success -> {
                val articles = response.data.articles.map { it.toDomain() }
                LoadResult.Page(
                    data = articles,
                    prevKey = if (page == INITIAL_PAGE) null else page - 1,
                    nextKey = if (articles.isEmpty()) null else page + 1
                )
            }

            is NetworkResponse.Failure -> {
                LoadResult.Error(Exception(response.error.message))
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
