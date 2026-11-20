package com.kev.instantsystem.ui.bookmarks

import androidx.lifecycle.ViewModel
import com.kev.instantsystem.data.bookmarks.BookmarkRepository
import com.kev.instantsystem.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: BookmarkRepository
) : ViewModel() {

    val bookmarks: StateFlow<List<Article>> = repository.bookmarks

    /** Returns true when the article is now saved. */
    fun toggle(article: Article): Boolean = repository.toggle(article)
}
