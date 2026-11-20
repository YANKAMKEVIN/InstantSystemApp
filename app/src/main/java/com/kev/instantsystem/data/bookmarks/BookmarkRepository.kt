package com.kev.instantsystem.data.bookmarks

import com.google.gson.Gson
import com.kev.instantsystem.domain.model.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Articles saved by the user, stored on the device. The most recently saved article comes first,
 * and an article is identified by its URL.
 */
@Singleton
class BookmarkRepository @Inject constructor(
    private val storage: BookmarkStorage
) {
    private val gson = Gson()
    private val _bookmarks = MutableStateFlow(load())
    val bookmarks: StateFlow<List<Article>> = _bookmarks.asStateFlow()

    fun isBookmarked(article: Article): Boolean = _bookmarks.value.any { it.url == article.url }

    /** Saves the article, or removes it if it was already saved. Returns true when it is now saved. */
    @Synchronized
    fun toggle(article: Article): Boolean {
        val current = _bookmarks.value
        val saved = current.none { it.url == article.url }
        val updated = if (saved) listOf(article) + current else current.filterNot { it.url == article.url }
        _bookmarks.value = updated
        storage.write(gson.toJson(updated.toTypedArray()))
        return saved
    }

    private fun load(): List<Article> = try {
        storage.read()
            ?.let { gson.fromJson(it, Array<Article>::class.java) }
            ?.toList()
            .orEmpty()
    } catch (e: Exception) {
        emptyList()
    }
}
