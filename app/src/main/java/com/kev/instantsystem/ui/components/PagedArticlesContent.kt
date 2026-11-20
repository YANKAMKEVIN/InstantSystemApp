package com.kev.instantsystem.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kev.instantsystem.R
import com.kev.instantsystem.domain.model.Article

/** What a paged news list shows. */
enum class ListContent { Skeleton, Error, Empty, Articles }

/**
 * Articles already on screen stay visible while the list reloads (pull to refresh, language
 * change) or when that reload fails; the skeleton, error and empty states only replace an empty list.
 */
fun resolveListContent(refresh: LoadState, itemCount: Int): ListContent = when {
    itemCount > 0 -> ListContent.Articles
    refresh is LoadState.Loading -> ListContent.Skeleton
    refresh is LoadState.Error -> ListContent.Error
    else -> ListContent.Empty
}

@Composable
fun PagedArticlesContent(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onArticleLongClick: ((Article) -> Unit)? = null,
    emptyContent: @Composable () -> Unit = {
        EmptyListPlaceholder(
            buttonText = stringResource(R.string.retry),
            onButtonClick = { articles.retry() }
        )
    }
) {
    val refresh = articles.loadState.refresh
    when (resolveListContent(refresh, articles.itemCount)) {
        ListContent.Skeleton -> LoaderScreen(state = LoaderState.Loading)

        ListContent.Error -> LoaderScreen(
            state = LoaderState.Error,
            errorMessage = (refresh as? LoadState.Error)?.error?.message
                ?: stringResource(R.string.error_unknown),
            onRetry = { articles.retry() }
        )

        ListContent.Empty -> emptyContent()

        ListContent.Articles -> NewsListScreenPaging(
            articles = articles,
            onArticleClick = onArticleClick,
            onArticleLongClick = onArticleLongClick
        )
    }
}
