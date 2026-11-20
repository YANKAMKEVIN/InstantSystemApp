package com.kev.instantsystem.ui.components

import androidx.compose.ui.res.stringResource
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.kev.instantsystem.R
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.bookmarks.BookmarksViewModel
import com.kev.instantsystem.ui.details.ArticleDetailsScreen
import com.kev.instantsystem.ui.utils.openCustomTab

@Composable
fun NewsDetailPane(
    article: Article?,
    onBack: (() -> Unit)? = null,
    bookmarksViewModel: BookmarksViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val bookmarks by bookmarksViewModel.bookmarks.collectAsState()
    if (article != null) {
        ArticleDetailsScreen(
            article = article,
            isBookmarked = bookmarks.any { it.url == article.url },
            onToggleBookmark = { bookmarksViewModel.toggle(article) },
            onShare = { shareArticle(context, article) },
            onBack = onBack,
            onReadMoreClick = {
                openCustomTab(context, article.url)
            })
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.detail_select_article))
        }
    }

}

private fun shareArticle(context: Context, article: Article) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, article.title)
        putExtra(Intent.EXTRA_TEXT, "${article.title}\n${article.url}")
    }
    context.startActivity(Intent.createChooser(intent, context.getString(R.string.detail_share)))
}
