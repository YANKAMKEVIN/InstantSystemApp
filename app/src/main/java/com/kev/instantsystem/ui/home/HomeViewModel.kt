package com.kev.instantsystem.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import com.kev.instantsystem.ui.components.RefreshFailureReporter
import com.kev.instantsystem.ui.components.RefreshFailures
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic of the Journal screen.
 * Fetches paginated news articles using the "everything" endpoint.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchEverythingUseCase: SearchEverythingUseCase
) : ViewModel(), RefreshFailureReporter by RefreshFailures() {

    /**
     * Starts from the device language, so the first search is already the right one (it used to
     * start in "fr" and be replaced as soon as [HomeRoute] passed the device language).
     */
    private val _language = MutableStateFlow(newsApiLanguage(Locale.getDefault().language))
    val language = _language

    /** Same language as now: no new search. Unsupported languages fall back to English. */
    fun setLanguage(lang: String) {
        _language.value = newsApiLanguage(lang)
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
