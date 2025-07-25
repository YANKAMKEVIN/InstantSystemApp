package com.kev.instantsystem.ui.search

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
import com.kev.instantsystem.ui.components.LoaderScreen
import com.kev.instantsystem.ui.components.LoaderState
import com.kev.instantsystem.ui.home.NewsDetailPane
import com.kev.instantsystem.ui.home.NewsListScreenPaging
import kotlinx.coroutines.launch

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query = viewModel.query.collectAsState().value
    val articles = viewModel.searchResults.collectAsLazyPagingItems()
    SearchScreen(
        query = query,
        onQueryChange = viewModel::onQueryChanged,
        articles = articles
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SearchScreen(
    query: String,
    onQueryChange: (String) -> Unit,
    articles: LazyPagingItems<Article>,
    modifier: Modifier = Modifier
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
                    label = { Text("Search news...") },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )

                // Show placeholder until the query is valid
                if (query.length < 3) {
                    Text(
                        text = "Start typing to search...",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        // style = MaterialTheme.typography.bodyLarge
                    )
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
                            errorMessage = error.message ?: "Unknown error",
                            onRetry = { articles.retry() }
                        )
                    }

                    else -> {
                        NewsListScreenPaging(articles = articles) { article ->
                            scope.launch {
                                navigator.navigateTo(
                                    pane = ListDetailPaneScaffoldRole.Detail,
                                    contentKey = article
                                )
                            }
                        }
                    }
                }
            }
        },
        detailPane = {
            val content = navigator.currentDestination?.contentKey as Article?
            AnimatedPane {
                NewsDetailPane(
                    article = content,
                )
            }
        }
    )
}