package com.kev.instantsystem.ui.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {

    val categories = listOf(
        ArticleCategory.Latest,
        ArticleCategory.Science,
        ArticleCategory.Sports,
        ArticleCategory.Technology,
        ArticleCategory.Health,
        ArticleCategory.Business
    )

    val articlesMap: Map<ArticleCategory, Flow<PagingData<Article>>> =
        categories.associateWith { getArticles(it.category) }

    /** When each cached list was received, shown when it cannot be reloaded (offline). */
    val updatedAtMap: Map<ArticleCategory, Flow<Long?>> =
        categories.associateWith { getTopHeadlinesUseCase.updatedAt(country = COUNTRY, category = it.category) }

    private fun getArticles(category: String?): Flow<PagingData<Article>> {
        return getTopHeadlinesUseCase(
            country = COUNTRY,
            category = category,
            query = null
        ).cachedIn(viewModelScope)
    }

    companion object {
        /** Top headlines stay on the United States (Kevin's decision, 21/11). */
        const val COUNTRY = "us"
    }
}
