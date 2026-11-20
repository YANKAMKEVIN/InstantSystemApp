package com.kev.instantsystem.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel @Inject constructor(
    private val searchEverythingUseCase: SearchEverythingUseCase
) : ViewModel() {

    // Holds the current user query
    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()


    // Expose paged search results based on the query

    val searchResults: Flow<PagingData<Article>> = _query
        .filter { it.trim().length >= MIN_QUERY_LENGTH } // Avoid calling too early
        .debounce(400)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            searchEverythingUseCase(query = query)
        }
        .cachedIn(viewModelScope)

    // Searches validated during this session (keyboard search action or topic chip), newest first
    private val _recentSearches = MutableStateFlow<List<String>>(emptyList())
    val recentSearches = _recentSearches.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }

    /** The user validated the current query with the keyboard search action. */
    fun onSearchSubmitted() {
        _recentSearches.update { addRecentSearch(it, _query.value) }
    }

    /** The user tapped a topic or a recent search: it becomes the query. */
    fun onSuggestionSelected(suggestion: String) {
        _query.value = suggestion
        _recentSearches.update { addRecentSearch(it, suggestion) }
    }
}

/**
 * Puts [query] at the top of [recent] (without duplicates, ignoring case) and keeps the
 * [max] latest ones. Queries too short to be searched are ignored.
 */
fun addRecentSearch(recent: List<String>, query: String, max: Int = 5): List<String> {
    val cleaned = query.trim()
    if (cleaned.length < MIN_QUERY_LENGTH) return recent
    return (listOf(cleaned) + recent.filterNot { it.equals(cleaned, ignoreCase = true) }).take(max)
}

const val MIN_QUERY_LENGTH = 3
