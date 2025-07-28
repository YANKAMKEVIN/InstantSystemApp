package com.kev.instantsystem.ui.home

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
class HomeViewModel @Inject constructor(
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

    private fun getArticles(category: String?): Flow<PagingData<Article>> {
        return getTopHeadlinesUseCase(
            country = "us",
            category = category,
            query = null
        ).cachedIn(viewModelScope)
    }

}