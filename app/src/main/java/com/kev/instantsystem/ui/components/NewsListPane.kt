package com.kev.instantsystem.ui.components

import androidx.compose.ui.res.stringResource
import com.kev.instantsystem.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import com.kev.instantsystem.ui.headlines.ArticleCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun NewsListPane(
    categories: List<ArticleCategory>,
    articlesMap: Map<ArticleCategory, Flow<PagingData<Article>>>,
    onArticleClick: (Article) -> Unit,
    onArticleLongClick: ((Article) -> Unit)? = null,
    updatedAtMap: Map<ArticleCategory, Flow<Long?>> = emptyMap()
) {
    ScrollableTabRowComponent(
        tabs = categories.map { stringResource(it.labelRes) },
        contentScreens = categories.map { category ->
            {
                NewsCategoryContent(
                    articlesFlow = articlesMap[category],
                    updatedAtFlow = updatedAtMap[category],
                    onArticleClick = onArticleClick,
                    onArticleLongClick = onArticleLongClick
                )
            }
        }
    )
}

@Composable
private fun NewsCategoryContent(
    articlesFlow: Flow<PagingData<Article>>?,
    onArticleClick: (Article) -> Unit,
    onArticleLongClick: ((Article) -> Unit)? = null,
    updatedAtFlow: Flow<Long?>? = null
) {
    if (articlesFlow == null) {
        Text(stringResource(R.string.category_empty))
    } else {
        val articles = articlesFlow.collectAsLazyPagingItems()
        val updatedAt = updatedAtFlow?.collectAsState(initial = null)?.value
        PagedArticlesContent(
            articles = articles,
            cachedAt = updatedAt,
            onArticleClick = onArticleClick,
            onArticleLongClick = onArticleLongClick
        )
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



