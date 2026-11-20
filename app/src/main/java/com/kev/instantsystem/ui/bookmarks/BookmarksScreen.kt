package com.kev.instantsystem.ui.bookmarks

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.kev.instantsystem.R
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.components.EmptyListPlaceholder
import com.kev.instantsystem.ui.components.ListArticleCard
import com.kev.instantsystem.ui.components.NewsDetailPane
import kotlinx.coroutines.launch

@Composable
fun BookmarksRoute(
    onDetailVisibilityChanged: (Boolean) -> Unit,
    viewModel: BookmarksViewModel = hiltViewModel()
) {
    val bookmarks by viewModel.bookmarks.collectAsState()
    BookmarksScreen(
        bookmarks = bookmarks,
        onDetailVisibilityChanged = onDetailVisibilityChanged,
        onArticleLongClick = rememberBookmarkToggle(viewModel)
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BookmarksScreen(
    bookmarks: List<Article>,
    onDetailVisibilityChanged: (Boolean) -> Unit,
    onArticleLongClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        modifier = modifier.fillMaxSize(),
        navigator = navigator,
        listPane = {
            if (bookmarks.isEmpty()) {
                EmptyListPlaceholder(
                    title = stringResource(R.string.bookmarks_empty_title),
                    subtitle = stringResource(R.string.bookmarks_empty_subtitle),
                    imageResId = R.drawable.ic_state_bookmarks
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(bookmarks, key = { it.url }) { article ->
                        ListArticleCard(
                            article = article,
                            onClick = {
                                scope.launch {
                                    navigator.navigateTo(
                                        pane = ListDetailPaneScaffoldRole.Detail,
                                        contentKey = article
                                    )
                                }
                            },
                            onLongClick = { onArticleLongClick(article) }
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
