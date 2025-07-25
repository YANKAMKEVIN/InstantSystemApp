package com.kev.ynfluence.ui.screens.news.ui.discovery

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
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.kev.instantsystem.ui.discovery.DiscoveryListPane
import com.kev.instantsystem.ui.discovery.DiscoveryViewModel
import com.kev.instantsystem.ui.home.NewsDetailPane
import com.kev.instantsystem.ui.model.Article
import com.kev.instantsystem.ui.model.Source
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun DiscoveryRoute(
    modifier: Modifier = Modifier,
    viewModel: DiscoveryViewModel = hiltViewModel()
) {
    val articles = viewModel.discoveryArticles.collectAsLazyPagingItems()
    DiscoveryScreen(modifier = modifier, articles = articles)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun DiscoveryScreen(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<Article>
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        modifier = modifier.fillMaxSize(),
        navigator = navigator,
        listPane = {
            DiscoveryListPane(
                articles = articles,
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
            AnimatedPane {
                NewsDetailPane(
                    article = content,
                )
            }
        }
    )
}

@Preview(showBackground = true, name = "DiscoveryScreen Preview")
@Composable
private fun PreviewDiscoveryScreen() {
    val fakeArticles = flowOf(
        PagingData.from(
            listOf(
                Article(
                    title = "Titre fictif",
                    description = "Ceci est un article fictif.",
                    content = "Contenu complet fictif...",
                    url = "https://example.com/fake",
                    publishedAt = "2025-07-24T12:00:00Z",
                    source = Source(
                        "fake-id",
                        "Source Fictive"
                    ),
                    author = "Auteur Test",
                    urlToImage = null
                )
            )
        )
    ).collectAsLazyPagingItems()

    DiscoveryScreen(articles = fakeArticles)
}
