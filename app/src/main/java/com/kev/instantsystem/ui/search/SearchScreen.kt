package com.kev.instantsystem.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kev.instantsystem.R
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.bookmarks.rememberBookmarkToggle
import com.kev.instantsystem.ui.components.EmptyListPlaceholder
import com.kev.instantsystem.ui.components.NewsDetailPane
import com.kev.instantsystem.ui.components.PagedArticlesContent
import com.kev.instantsystem.ui.components.RefreshFailureReporter
import com.kev.instantsystem.ui.placeholder.ScreenPlaceholder
import kotlinx.coroutines.launch

@Composable
fun SearchRoute(
    onDetailVisibilityChanged: (Boolean) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query = viewModel.query.collectAsState().value
    val recentSearches = viewModel.recentSearches.collectAsState().value
    val articles = viewModel.searchResults.collectAsLazyPagingItems()
    SearchScreen(
        query = query,
        onDetailVisibilityChanged = onDetailVisibilityChanged,
        onQueryChange = viewModel::onQueryChanged,
        recentSearches = recentSearches,
        onSearch = viewModel::onSearchSubmitted,
        onSuggestionSelected = viewModel::onSuggestionSelected,
        articles = articles,
        onArticleLongClick = rememberBookmarkToggle(),
        refreshFailures = viewModel
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
    recentSearches: List<String> = emptyList(),
    onSearch: () -> Unit = {},
    onSuggestionSelected: (String) -> Unit = onQueryChange,
    onArticleLongClick: ((Article) -> Unit)? = null,
    refreshFailures: RefreshFailureReporter? = null,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Ready to type when the tab opens on an empty search.
    LaunchedEffect(Unit) {
        if (query.isEmpty()) focusRequester.requestFocus()
    }

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
                        .padding(16.dp)
                        .focusRequester(focusRequester),
                    label = { Text(stringResource(R.string.search_hint)) },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.search_clear)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        onSearch()
                        keyboardController?.hide()
                    })
                )

                // Suggestions and hint until the query is long enough to be searched
                if (query.trim().length < MIN_QUERY_LENGTH) {
                    SearchSuggestions(
                        recentSearches = recentSearches,
                        onSuggestionSelected = {
                            onSuggestionSelected(it)
                            keyboardController?.hide()
                        }
                    )
                    ScreenPlaceholder(
                        title = stringResource(R.string.search_min_chars),
                        iconRes = R.drawable.ic_state_search
                    )
                    return@Column
                }

                PagedArticlesContent(
                    articles = articles,
                    refreshFailures = refreshFailures,
                    listKey = SEARCH_LIST_KEY,
                    onArticleLongClick = onArticleLongClick,
                    onArticleClick = { article ->
                        scope.launch {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                contentKey = article
                            )
                        }
                    },
                    emptyContent = { EmptyListPlaceholder() }
                )
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
const val SEARCH_LIST_KEY = "search"
