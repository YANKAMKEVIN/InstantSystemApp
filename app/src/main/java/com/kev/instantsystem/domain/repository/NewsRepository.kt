package com.kev.instantsystem.domain.repository

import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface defining operations for fetching news articles.
 *
 * This abstraction allows for decoupling between the domain layer and data layer,
 * enabling easier testing and substitution of data sources.
 */
interface NewsRepository {

    /**
     * Retrieves top headlines with optional filters such as country, category, and search query.
     *
     * This uses the `/v2/top-headlines` endpoint from the News API.
     *
     * @param country ISO 3166-1 country code (e.g., "fr", "us")
     * @param category News category (e.g., "sports", "technology")
     * @param query Optional keyword or phrase for search
     * @return A [Flow] emitting [PagingData] of [Article]s
     */
    fun getTopHeadlines(
        country: String,
        category: String? = null,
        query: String? = null
    ): Flow<PagingData<Article>>

    /**
     * Performs a full-text search for articles across all sources using the `/v2/everything` endpoint.
     *
     * This is useful for more advanced search and discovery scenarios.
     *
     * @param query Required search string or phrase
     * @param sortBy Optional sorting ("relevancy", "popularity", "publishedAt")
     * @param from Optional start date (ISO 8601 format)
     * @param to Optional end date (ISO 8601 format)
     * @param language Optional 2-letter language code (e.g., "en", "fr")
     * @return A [Flow] emitting [PagingData] of [Article]s
     */
    fun searchEverything(
        query: String,
        sortBy: String? = null,
        from: String? = null,
        to: String? = null,
        language: String? = null
    ): Flow<PagingData<Article>>
}
