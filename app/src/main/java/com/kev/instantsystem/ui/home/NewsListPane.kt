package com.kev.instantsystem.ui.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.ui.components.EmptyListPlaceholder
import com.kev.instantsystem.ui.components.LoaderScreen
import com.kev.instantsystem.ui.components.LoaderState
import com.kev.instantsystem.ui.components.ScrollableTabRowComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun NewsListPane(
    categories: List<ArticleCategory>,
    articlesMap: Map<ArticleCategory, Flow<PagingData<Article>>>,
    onArticleClick: (Article) -> Unit
) {
    ScrollableTabRowComponent(
        tabs = categories.map { it.label },
        contentScreens = categories.map { category ->
            {
                NewsCategoryContent(
                    articlesFlow = articlesMap[category],
                    onArticleClick = onArticleClick
                )
            }
        }
    )
}

@Composable
private fun NewsCategoryContent(
    articlesFlow: Flow<PagingData<Article>>?,
    onArticleClick: (Article) -> Unit
) {
    if (articlesFlow == null) {
        Text("Aucun article pour cette catégorie")
    } else {
        val articles = articlesFlow.collectAsLazyPagingItems()

        when (articles.loadState.refresh) {
            is LoadState.Loading -> LoaderScreen(state = LoaderState.Loading)

            is LoadState.Error -> {
                val error = (articles.loadState.refresh as LoadState.Error).error
                LoaderScreen(
                    state = LoaderState.Error,
                    errorMessage = error.message ?: "Erreur inconnue",
                    onRetry = { articles.retry() }
                )
            }

            else -> {
                if (articles.itemCount == 0) {
                    EmptyListPlaceholder(
                        buttonText = "Réessayer",
                        onButtonClick = { articles.retry() }
                    )
                } else {
                    NewsListScreenPaging(
                        articles = articles,
                        onArticleClick = onArticleClick
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewNewsCategoryContent() {
    val sampleArticle = Article(
        title = "Live - Une nouvelle manifestation à Paris",
        description = "Des milliers de personnes défilent...",
        content = "Des informations en continu sont attendues...",
        url = "https://example.com/live",
        publishedAt = "2025-07-24T12:00:00Z",
        source = Source("Le Monde", "lemonde"),
        author = "Jean Dupont",
        urlToImage = "https://placehold.co/600x400"
    )

    val fakeFlow: Flow<PagingData<Article>> = flowOf(PagingData.from(listOf(sampleArticle)))

    NewsCategoryContent(
        articlesFlow = fakeFlow,
        onArticleClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewNewsListPane() {
    val sampleArticle = Article(
        title = "Live - Le climat mondial en alerte",
        description = "Des experts alertent sur le réchauffement...",
        content = "Les températures mondiales ont atteint un seuil...",
        url = "https://example.com/climat",
        publishedAt = "2025-07-24T12:00:00Z",
        source = Source("France Info", "frinfo"),
        author = "Marie Dubois",
        urlToImage = "https://placehold.co/600x400"
    )

    val fakeFlow = flowOf(PagingData.from(listOf(sampleArticle)))
    val mockMap = listOf(
        ArticleCategory.Health,
        ArticleCategory.Business,
        ArticleCategory.Latest,
        ArticleCategory.Sports,
        ArticleCategory.Science,
        ArticleCategory.Technology
    ).associateWith { fakeFlow }

    NewsListPane(
        categories = listOf(
            ArticleCategory.Health,
            ArticleCategory.Business,
            ArticleCategory.Latest,
            ArticleCategory.Sports,
            ArticleCategory.Science,
            ArticleCategory.Technology
        ),
        articlesMap = mockMap,
        onArticleClick = {}
    )
}



