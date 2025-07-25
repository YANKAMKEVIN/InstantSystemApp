package com.kev.instantsystem.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kev.ynfluence.ui.screens.news.domain.usecase.GetTopHeadlinesUseCase
import com.kev.instantsystem.ui.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
) : ViewModel() {

    val categories = listOf(
        ArticleCategory.Latest,
        ArticleCategory.Trump,
        ArticleCategory.Ukraine,
        ArticleCategory.Commerce,
        ArticleCategory.Africa,
        ArticleCategory.Israel
    )

    val articlesMap: Map<ArticleCategory, Flow<PagingData<Article>>> =
        listOf(
            ArticleCategory.Latest,
            ArticleCategory.Trump,
            ArticleCategory.Ukraine,
            ArticleCategory.Commerce,
            ArticleCategory.Africa,
            ArticleCategory.Israel
        ).associateWith { getArticles(it.query) }

    private fun getArticles(query: String?): Flow<PagingData<Article>> {
        return getTopHeadlinesUseCase(
            country = "us",
            category = query,
            query = query
        ).cachedIn(viewModelScope)
    }

}