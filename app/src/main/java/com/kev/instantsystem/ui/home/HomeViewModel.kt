package com.kev.instantsystem.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
     * Public stream of articles based on a fixed or dynamic search.
     * For now, we provide a default query (e.g., "technology").
     */
    val discoveryArticles: Flow<PagingData<Article>> = searchEverythingUseCase(
        query = "technology",
        language = language.value
    ).cachedIn(viewModelScope)
}
