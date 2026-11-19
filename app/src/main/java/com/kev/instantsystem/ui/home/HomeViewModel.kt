package com.kev.instantsystem.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic of the Journal screen.
 * Fetches paginated news articles using the "everything" endpoint.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchEverythingUseCase: SearchEverythingUseCase
) : ViewModel() {

    private val _language = MutableStateFlow("fr")
    val language = _language

    fun setLanguage(lang: String) {
        _language.value = lang
    }

    /**
     * Public stream of articles for a default query (e.g., "technology").
     * Rebuilt for every language change: the previous search is cancelled and a new one starts.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val discoveryArticles: Flow<PagingData<Article>> = _language
        .flatMapLatest { lang ->
            searchEverythingUseCase(
                query = "technology",
                language = lang
            )
        }
        .cachedIn(viewModelScope)
}
