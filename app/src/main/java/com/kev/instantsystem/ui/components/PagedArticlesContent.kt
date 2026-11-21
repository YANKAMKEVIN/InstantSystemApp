package com.kev.instantsystem.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.kev.instantsystem.R
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.utils.formatCacheDate

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

/**
 * The articles on screen could not be reloaded and come from the offline cache received at
 * [cachedAt]: its date is shown above the list. Null when there is nothing to say.
 */
fun cachedListDate(refresh: LoadState, itemCount: Int, cachedAt: Long?): Long? =
    cachedAt?.takeIf { refresh is LoadState.Error && itemCount > 0 }

@Composable
fun PagedArticlesContent(
    articles: LazyPagingItems<Article>,
    onArticleClick: (Article) -> Unit,
    onArticleLongClick: ((Article) -> Unit)? = null,
    /** When the cached list was received (top headlines only), see [cachedListDate]. */
    cachedAt: Long? = null,
    /** Shows a failed reload of the articles on screen; [listKey] tells this list from the others. */
    refreshFailures: RefreshFailureReporter? = null,
    listKey: String = "",
    emptyContent: @Composable () -> Unit = {
        EmptyListPlaceholder(
            buttonText = stringResource(R.string.retry),
            onButtonClick = { articles.retry() }
        )
    }
) {
    val refresh = articles.loadState.refresh
    LaunchedEffect(refreshFailures, listKey, refresh) {
        refreshFailures?.onListLoadState(listKey, refresh, articles.itemCount)
    }
    when (resolveListContent(refresh, articles.itemCount)) {
        ListContent.Skeleton -> LoaderScreen(state = LoaderState.Loading)

        ListContent.Error -> LoaderScreen(
            state = LoaderState.Error,
            errorMessage = stringResource(errorMessageRes((refresh as? LoadState.Error)?.error)),
            onRetry = { articles.retry() }
        )

        ListContent.Empty -> emptyContent()

        ListContent.Articles -> Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                cachedListDate(refresh, articles.itemCount, cachedAt)?.let { date ->
                    CachedListBanner(date)
                }
                NewsListScreenPaging(
                    articles = articles,
                    onArticleClick = onArticleClick,
                    onArticleLongClick = onArticleLongClick
                )
            }
            if (refreshFailures != null) {
                RefreshFailureSnackbar(
                    refreshFailures = refreshFailures,
                    listKey = listKey,
                    onRetry = { articles.refresh() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}

/** "Last list received: Nov 21 at 18:04", above a list that comes from the offline cache. */
@Composable
private fun CachedListBanner(cachedAt: Long) {
    Text(
        text = stringResource(R.string.headlines_cached_at, formatCacheDate(cachedAt)),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall
    )
}

/** "Couldn't refresh the list." at the bottom of the list, with "Retry", as in the aviv app. */
@Composable
private fun RefreshFailureSnackbar(
    refreshFailures: RefreshFailureReporter,
    listKey: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val failure = refreshFailures.refreshFailure.collectAsState().value?.takeIf { it.listKey == listKey }

    failure?.let {
        LaunchedEffect(it) {
            val result = snackbarHostState.showSnackbar(
                message = context.getString(refreshErrorMessageRes(it.error)),
                actionLabel = context.getString(R.string.retry),
                duration = SnackbarDuration.Long
            )
            refreshFailures.onRefreshFailureShown(it)
            if (result == SnackbarResult.ActionPerformed) onRetry()
        }
    }
    SnackbarHost(hostState = snackbarHostState, modifier = modifier)
}
