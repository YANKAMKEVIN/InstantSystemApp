package com.kev.instantsystem.domain.usecase

import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * UseCase for searching all available news articles via the `/everything` endpoint.
 *
 * This is ideal for full-text queries across titles, descriptions, and content fields.
 *
 * @param repository The news repository handling data operations.
 */
class SearchEverythingUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    /**
     * Invoke the use case.
     *
     * @param query The keyword(s) to search for. Required.
     * @param sortBy Optional sort order (e.g., "relevancy", "popularity", "publishedAt")
     * @param from Optional oldest date (ISO 8601)
     * @param to Optional newest date (ISO 8601)
     * @param language Optional language filter (e.g., "fr", "en")
     *
     * @return A [Flow] of [PagingData] containing search results.
     */
    operator fun invoke(
        query: String,
        sortBy: String? = null,
        from: String? = null,
        to: String? = null,
        language: String? = null,
    ): Flow<PagingData<Article>> {
        return repository.searchEverything(
            query = query,
            sortBy = sortBy,
            from = from,
            to = to,
            language = language
        )
    }
}
