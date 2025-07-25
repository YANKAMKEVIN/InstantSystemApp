package com.kev.instantsystem.data.repository


import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kev.instantsystem.data.paging.EverythingPagingSourceFactory
import com.kev.instantsystem.data.paging.NewsPagingSourceFactory
import com.kev.ynfluence.ui.screens.news.domain.repository.NewsRepository
import com.kev.ynfluence.ui.screens.news.ui.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of [NewsRepository] that provides paginated access to news articles.
 *
 * This class delegates PagingSource creation to the appropriate factories.
 * It handles both "top-headlines" and "everything" endpoints from the News API.
 */
class NewsRepositoryImpl(
    private val headlinesPagingSourceFactory: NewsPagingSourceFactory,
    private val everythingPagingSourceFactory: EverythingPagingSourceFactory
) : NewsRepository {

    /**
     * Fetches paginated top headlines based on optional filters.
     *
     * @param country ISO country code (e.g., "fr")
     * @param category News category (e.g., "technology")
     * @param query Optional keyword for search
     * @return A [Flow] of [PagingData] containing [Article] items.
     */
    override fun getTopHeadlines(
        country: String,
        category: String?,
        query: String?
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                headlinesPagingSourceFactory.create(
                    country = country,
                    category = category,
                    query = query
                )
            }
        ).flow
    }

    /**
     * Fetches paginated results using the `/v2/everything` endpoint.
     *
     * Supports advanced filtering like sorting, date range, and language.
     *
     * @param query Required search term
     * @param sortBy Sorting method (e.g., "relevancy", "publishedAt")
     * @param from Start date in ISO 8601 format
     * @param to End date in ISO 8601 format
     * @param language 2-letter language code (e.g., "en", "fr")
     * @return A [Flow] of [PagingData] containing [Article] items.
     */
    override fun searchEverything(
        query: String,
        sortBy: String?,
        from: String?,
        to: String?,
        language: String?
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                everythingPagingSourceFactory.create(
                    query = query,
                    sortBy = sortBy,
                    from = from,
                    to = to,
                    language = language
                )
            }
        ).flow
    }
}

