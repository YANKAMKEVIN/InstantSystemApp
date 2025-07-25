package com.kev.instantsystem.data.remote

import com.kev.instantsystem.data.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    /**
     * Fetches the top headlines from the News API.
     *
     * You must provide either:
     * - a [country] (2-letter ISO code) or
     * - a [category] or
     * - a keyword [query]
     *
     * Notes:
     * - `country`, `category`, and `query` cannot be used with `sources` (not yet supported here).
     * - Results are paginated via [page] and [pageSize].
     *
     * @param apiKey Your API key.
     * @param country Optional ISO 3166-1 code (e.g. "fr", "us").
     * @param category Optional category (e.g. "business", "technology").
     * @param query Optional keyword or phrase to search for.
     * @param page Page number (starting at 1).
     * @param pageSize Number of results per page (max 100).
     *
     * @return A [NewsDto] containing a list of articles and metadata.
     */
    @GET(ENDPOINT_TOP_HEADLINES)
    suspend fun getTopHeadlines(
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_COUNTRY) country: String? = null,
        @Query(QUERY_CATEGORY) category: String? = null,
        @Query(QUERY_QUERY) query: String? = null,
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY_PAGE_SIZE) pageSize: Int
    ): NewsDto

    /**
     * Fetches all articles matching the given query from the News API.
     *
     * This endpoint allows searching across millions of articles from over 150,000 sources.
     * Ideal for building advanced search or discovery features.
     *
     * Notes:
     * - The [query] parameter is required and supports advanced search operators:
     *   - Quotes for exact phrases (e.g. "climate change")
     *   - `+` to require terms, `-` to exclude, AND/OR/NOT operators
     * - You can narrow results with optional filters like:
     *   - [sortBy] (e.g. "relevancy", "popularity", "publishedAt")
     *   - [from] and [to] for date filtering (ISO 8601 format)
     *   - [language] for results in a specific language (e.g. "fr", "en")
     * - Results are paginated via [page] and [pageSize].
     *
     * @param apiKey Your API key.
     * @param query Keyword(s) or phrase to search for (mandatory).
     * @param sortBy Sorting method: "relevancy", "popularity", or "publishedAt" (optional).
     * @param from Earliest date allowed (ISO 8601), e.g. "2025-07-20" (optional).
     * @param to Latest date allowed (ISO 8601), e.g. "2025-07-23T18:00:00Z" (optional).
     * @param language Restrict results to a language (e.g. "fr", "en") (optional).
     * @param page Page number (starting at 1).
     * @param pageSize Number of results per page (max 100).
     *
     * @return A [NewsDto] containing a list of articles and metadata.
     */
    @GET(ENDPOINT_EVERYTHING)
    suspend fun getEverything(
        @Query(QUERY_API_KEY) apiKey: String,
        @Query(QUERY_QUERY) query: String,
        @Query(QUERY_SORT_BY) sortBy: String? = null,
        @Query(QUERY_FROM) from: String? = null,
        @Query(QUERY_TO) to: String? = null,
        @Query(QUERY_LANGUAGE) language: String? = null,
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY_PAGE_SIZE) pageSize: Int
    ): NewsDto

    companion object {
        const val ENDPOINT_TOP_HEADLINES = "v2/top-headlines"
        const val ENDPOINT_EVERYTHING = "v2/everything"

        const val QUERY_API_KEY = "apiKey"
        const val QUERY_COUNTRY = "country"
        const val QUERY_CATEGORY = "category"
        const val QUERY_QUERY = "q"
        const val QUERY_PAGE = "page"
        const val QUERY_PAGE_SIZE = "pageSize"

        const val QUERY_SORT_BY = "sortBy"
        const val QUERY_FROM = "from"
        const val QUERY_TO = "to"
        const val QUERY_LANGUAGE = "language"

    }
}
