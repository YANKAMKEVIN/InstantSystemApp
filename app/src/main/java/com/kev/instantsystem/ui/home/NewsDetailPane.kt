package com.kev.instantsystem.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.ui.details.ArticleDetailsScreen
import com.kev.instantsystem.ui.utils.openCustomTab

@Composable
fun NewsDetailPane(
    article: Article?,
) {
    val context = LocalContext.current
    if (article != null) {
        ArticleDetailsScreen(
            article = article,
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
            Text(text = "Sélectionnez un article")
        }
    }

}
