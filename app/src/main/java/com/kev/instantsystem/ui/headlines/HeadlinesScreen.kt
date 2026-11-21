package com.kev.instantsystem.ui.headlines

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.bookmarks.rememberBookmarkToggle
import com.kev.instantsystem.ui.components.NewsDetailPane
import com.kev.instantsystem.ui.components.NewsListPane
import com.kev.instantsystem.ui.components.RefreshFailureReporter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun HeadlinesRoute(
    modifier: Modifier = Modifier,
    onDetailVisibilityChanged: (Boolean) -> Unit,
    viewModel: HeadlinesViewModel = hiltViewModel()
) {
    HeadlinesScreen(
        modifier = modifier,
        onDetailVisibilityChanged = onDetailVisibilityChanged,
        categories = viewModel.categories,
        articlesMap = viewModel.articlesMap,
        updatedAtMap = viewModel.updatedAtMap,
        refreshFailures = viewModel,
        onArticleLongClick = rememberBookmarkToggle()
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HeadlinesScreen(
    modifier: Modifier = Modifier,
    onDetailVisibilityChanged: (Boolean) -> Unit,
    categories: List<ArticleCategory>,
    articlesMap: Map<ArticleCategory, Flow<PagingData<Article>>>,
    updatedAtMap: Map<ArticleCategory, Flow<Long?>> = emptyMap(),
    refreshFailures: RefreshFailureReporter? = null,
    onArticleLongClick: ((Article) -> Unit)? = null
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        modifier = modifier
            .fillMaxSize(),
        navigator = navigator,
        listPane = {
            NewsListPane(
                categories = categories,
                articlesMap = articlesMap,
                updatedAtMap = updatedAtMap,
                refreshFailures = refreshFailures,
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
        },
    )
}

@Preview(showBackground = true, name = "HomeScreen Preview")
@Composable
private fun PreviewHomeScreen() {
    val dummyCategories = listOf(
        ArticleCategory.Latest,
        ArticleCategory.Science,
        ArticleCategory.Sports
    )

    val dummyArticlesFlow = flowOf(PagingData.empty<Article>())

    val dummyArticlesMap = dummyCategories.associateWith { dummyArticlesFlow }

    HeadlinesScreen(
        onDetailVisibilityChanged = {},
        categories = dummyCategories,
        articlesMap = dummyArticlesMap
    )
}
