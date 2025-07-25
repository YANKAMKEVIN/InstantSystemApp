package com.kev.ynfluence.ui.screens.news.domain.usecase

import androidx.paging.PagingData
import com.kev.ynfluence.ui.screens.news.domain.repository.NewsRepository
import com.kev.ynfluence.ui.screens.news.ui.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving top headlines from the news API.
 *
 * This is typically used for showcasing trending or major news items.
 *
 * @property repository The repository that handles data access for news articles.
 */
class GetTopHeadlinesUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    /**
     * Invokes the use case to fetch a paginated list of top headlines.
     *
     * @param country ISO 3166-1 alpha-2 country code (e.g., "fr", "us").
     * @param category Optional news category to filter (e.g., "technology", "sports").
     * @param query Optional search keyword to filter headlines by relevance.
     *
     * @return A [Flow] emitting [PagingData] containing [Article] objects.
     */
    operator fun invoke(
        country: String,
        category: String? = null,
        query: String? = null
    ): Flow<PagingData<Article>> {
        return repository.getTopHeadlines(
            country = country,
            category = category,
            query = query
        )
    }
}
