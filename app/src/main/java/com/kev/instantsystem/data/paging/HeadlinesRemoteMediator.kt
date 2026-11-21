package com.kev.instantsystem.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.kev.instantsystem.data.local.HeadlineArticleEntity
import com.kev.instantsystem.data.local.HeadlinesDao
import com.kev.instantsystem.data.local.headlinesCacheKey
import com.kev.instantsystem.data.mapper.toDomain
import com.kev.instantsystem.data.network.NetworkException
import com.kev.instantsystem.data.network.NetworkResponse
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource

/**
 * Loads the top headlines from NewsAPI into the local cache, which the list reads from.
 * When NewsAPI cannot be reached, the error is reported and the last list received stays on screen.
 */
@OptIn(ExperimentalPagingApi::class)
class HeadlinesRemoteMediator(
    private val remoteDataSource: NewsRemoteDataSource,
    private val dao: HeadlinesDao,
    private val country: String,
    private val category: String?,
    private val now: () -> Long = System::currentTimeMillis
) : RemoteMediator<Int, HeadlineArticleEntity>() {

    private val cacheKey = headlinesCacheKey(country, category)

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HeadlineArticleEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> FIRST_PAGE
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> dao.list(cacheKey)?.nextPage
                ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        val result = remoteDataSource.getTopHeadlines(
            country = country,
            category = category,
            query = null,
            page = page,
            pageSize = state.config.pageSize
        )
        return when (result) {
            is NetworkResponse.Success -> {
                val articles = result.data.articles.map { it.toDomain() }
                val nextPage = if (articles.isEmpty()) null else page + 1
                if (loadType == LoadType.REFRESH) {
                    dao.replaceList(cacheKey, articles, nextPage, updatedAt = now())
                } else {
                    dao.appendPage(cacheKey, articles, nextPage)
                }
                MediatorResult.Success(endOfPaginationReached = nextPage == null)
            }

            is NetworkResponse.Failure -> MediatorResult.Error(NetworkException(result.error))
        }
    }

    private companion object {
        const val FIRST_PAGE = 1
    }
}
