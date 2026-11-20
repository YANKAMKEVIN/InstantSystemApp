package com.kev.instantsystem.ui.home

import androidx.compose.ui.res.stringResource
import com.kev.instantsystem.R
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.components.EmptyListPlaceholder
import com.kev.instantsystem.ui.components.LoaderScreen
import com.kev.instantsystem.ui.components.LoaderState
import com.kev.instantsystem.ui.components.NewsListScreenPaging

@Composable
fun HomeListPane(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onArticleLongClick: ((Article) -> Unit)? = null
) {
    when (articles.loadState.refresh) {
        is LoadState.Loading -> {
            LoaderScreen(state = LoaderState.Loading)
        }

        is LoadState.Error -> {
            val error = (articles.loadState.refresh as LoadState.Error).error
            LoaderScreen(
                state = LoaderState.Error,
                errorMessage = error.message ?: stringResource(R.string.error_unknown),
                onRetry = { articles.retry() }
            )
        }

        else -> {
            if (articles.itemCount == 0) {
                EmptyListPlaceholder(
                    buttonText = stringResource(R.string.retry),
                    onButtonClick = { articles.retry() }
                )
            }
            NewsListScreenPaging(
                articles = articles,
                onArticleClick = onArticleClick,
                onArticleLongClick = onArticleLongClick
            )
        }
    }
}
