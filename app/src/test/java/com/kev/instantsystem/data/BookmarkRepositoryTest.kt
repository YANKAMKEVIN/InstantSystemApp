package com.kev.instantsystem.data

import com.kev.instantsystem.data.bookmarks.BookmarkRepository
import com.kev.instantsystem.data.bookmarks.BookmarkStorage
import com.kev.instantsystem.domain.model.Article
import com.kev.instantsystem.domain.model.Source
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Plain JVM test (no Robolectric). */
class BookmarkRepositoryTest {

    private class InMemoryStorage(var value: String? = null) : BookmarkStorage {
        override fun read(): String? = value
        override fun write(value: String) {
            this.value = value
        }
    }

    private fun article(url: String, title: String = "Title $url") = Article(
        source = Source(id = null, name = "Le Monde"),
        author = null,
        title = title,
        description = null,
        url = url,
        urlToImage = null,
        publishedAt = "2025-07-24T12:00:00Z",
        content = null
    )

    @Test
    fun `toggle saves an article first, then removes it`() {
        val repository = BookmarkRepository(InMemoryStorage())
        val first = article("https://a")
        val second = article("https://b")

        assertTrue(repository.toggle(first))
        assertTrue(repository.toggle(second))
        assertEquals(listOf(second, first), repository.bookmarks.value)
        assertTrue(repository.isBookmarked(first))

        assertFalse(repository.toggle(first))
        assertEquals(listOf(second), repository.bookmarks.value)
        assertFalse(repository.isBookmarked(first))
    }

    @Test
    fun `articles are identified by their url`() {
        val repository = BookmarkRepository(InMemoryStorage())
        repository.toggle(article("https://a", title = "Old title"))

        assertTrue(repository.isBookmarked(article("https://a", title = "New title")))
        assertFalse(repository.toggle(article("https://a", title = "New title")))
        assertTrue(repository.bookmarks.value.isEmpty())
    }

    @Test
    fun `saved articles survive a restart`() {
        val storage = InMemoryStorage()
        BookmarkRepository(storage).apply {
            toggle(article("https://a"))
            toggle(article("https://b"))
        }

        val reloaded = BookmarkRepository(storage)

        assertEquals(listOf(article("https://b"), article("https://a")), reloaded.bookmarks.value)
    }

    @Test
    fun `unreadable storage starts with no bookmarks`() {
        val repository = BookmarkRepository(InMemoryStorage("not json"))

        assertTrue(repository.bookmarks.value.isEmpty())
    }
}
