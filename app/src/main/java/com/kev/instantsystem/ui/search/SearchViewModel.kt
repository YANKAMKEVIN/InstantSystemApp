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
        .filter { it.length >= 3 && it.isNotBlank() } // Avoid calling too early
        .debounce(400)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            searchEverythingUseCase(query = query)
        }
        .cachedIn(viewModelScope)

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }
}
