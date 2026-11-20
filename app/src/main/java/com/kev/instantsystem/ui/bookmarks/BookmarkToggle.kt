package com.kev.instantsystem.ui.bookmarks

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.kev.instantsystem.R
import com.kev.instantsystem.domain.model.Article

/** Saves or removes an article (long press on a card) and confirms it with a short message. */
@Composable
fun rememberBookmarkToggle(viewModel: BookmarksViewModel = hiltViewModel()): (Article) -> Unit {
    val context = LocalContext.current
    return remember(viewModel, context) {
        { article ->
            val saved = viewModel.toggle(article)
            Toast.makeText(
                context,
                if (saved) R.string.bookmark_added else R.string.bookmark_removed,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
