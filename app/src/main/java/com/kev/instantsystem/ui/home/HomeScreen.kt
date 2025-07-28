package com.kev.instantsystem.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    onDetailVisibilityChanged: (Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        modifier = modifier,
        onDetailVisibilityChanged = onDetailVisibilityChanged,
        categories = viewModel.categories,
        articlesMap = viewModel.articlesMap
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onDetailVisibilityChanged: (Boolean) -> Unit,
    categories: List<ArticleCategory>,
    articlesMap: Map<ArticleCategory, Flow<PagingData<Article>>>
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
            AnimatedPane {
                NewsDetailPane(
                    article = content,
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

    HomeScreen(
        onDetailVisibilityChanged = {},
        categories = dummyCategories,
        articlesMap = dummyArticlesMap
    )
}
