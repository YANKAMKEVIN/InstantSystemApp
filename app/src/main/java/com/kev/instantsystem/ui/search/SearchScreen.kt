package com.kev.instantsystem.ui.search

import androidx.compose.ui.res.stringResource
import com.kev.instantsystem.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.bookmarks.rememberBookmarkToggle
import com.kev.instantsystem.ui.components.EmptyListPlaceholder
import com.kev.instantsystem.ui.components.LoaderScreen
import com.kev.instantsystem.ui.components.LoaderState
import com.kev.instantsystem.ui.components.NewsDetailPane
import com.kev.instantsystem.ui.components.NewsListScreenPaging
import com.kev.instantsystem.ui.placeholder.ScreenPlaceholder
import kotlinx.coroutines.launch

@Composable
fun SearchRoute(
    onDetailVisibilityChanged: (Boolean) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query = viewModel.query.collectAsState().value
    val articles = viewModel.searchResults.collectAsLazyPagingItems()
    SearchScreen(
        query = query,
        onDetailVisibilityChanged = onDetailVisibilityChanged,
        onQueryChange = viewModel::onQueryChanged,
        articles = articles,
        onArticleLongClick = rememberBookmarkToggle()
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    query: String,
    onDetailVisibilityChanged: (Boolean) -> Unit,
    onQueryChange: (String) -> Unit,
    articles: LazyPagingItems<Article>,
    onArticleLongClick: ((Article) -> Unit)? = null,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        modifier = modifier.fillMaxSize(),
        navigator = navigator,
        listPane = {
            Column(modifier = Modifier.fillMaxSize()) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { onQueryChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    label = { Text(stringResource(R.string.search_hint)) },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )

                // Show placeholder until the query is valid
                if (query.length < 3) {
                    ScreenPlaceholder(stringResource(R.string.search_min_chars), "🔍")
                    return@Column
                }

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
                            EmptyListPlaceholder()
                        } else
                            NewsListScreenPaging(
                                articles = articles,
                                onArticleLongClick = onArticleLongClick,
                                onArticleClick = { article ->
                                    scope.launch {
                                        navigator.navigateTo(
                                            pane = ListDetailPaneScaffoldRole.Detail,
                                            contentKey = article
                                        )
                                    }
                                }
                            )
                    }
                }
            }
        },
        detailPane = {
            val content = navigator.currentDestination?.contentKey as Article?
            onDetailVisibilityChanged(content != null)
            val isSinglePane =
                navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Hidden
            AnimatedPane {
                NewsDetailPane(
                    article = content,
                    onBack = if (isSinglePane) {
                        { scope.launch { navigator.navigateBack() } }
                    } else null
                )
            }
        }
    )
}