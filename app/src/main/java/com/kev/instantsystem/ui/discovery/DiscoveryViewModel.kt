package com.kev.instantsystem.ui.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import com.kev.instantsystem.ui.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and logic of the Journal screen.
 * Fetches paginated news articles using the "everything" endpoint.
 */
@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    private val searchEverythingUseCase: SearchEverythingUseCase
) : ViewModel() {

    /**
     * Public stream of articles based on a fixed or dynamic search.
     * For now, we provide a default query (e.g., "technology").
     */
    val discoveryArticles: Flow<PagingData<Article>> = searchEverythingUseCase(
        query = "technology" // you can make this dynamic later
    ).cachedIn(viewModelScope)
}
