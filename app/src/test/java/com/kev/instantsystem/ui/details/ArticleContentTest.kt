package com.kev.instantsystem.ui.details

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleContentTest {

    @Test
    fun `the chars suffix added by NewsAPI is removed`() {
        assertEquals(
            "Les températures ont franchi un seuil critique…",
            cleanArticleContent("Les températures ont franchi un seuil critique… [+1234 chars]")
        )
        assertEquals("Short text", cleanArticleContent("Short text [+12 chars] "))
    }

    @Test
    fun `complete content is kept as is`() {
        assertEquals("A full paragraph.", cleanArticleContent("A full paragraph."))
        assertEquals("Price: [+5 %]", cleanArticleContent("Price: [+5 %]"))
    }

    @Test
    fun `empty content gives nothing to show`() {
        assertNull(cleanArticleContent(null))
        assertNull(cleanArticleContent("  [+300 chars]"))
    }

    @Test
    fun `truncation is detected from the suffix or the ellipsis`() {
        assertTrue(isTruncatedContent("Text… [+1234 chars]"))
        assertTrue(isTruncatedContent("Text…"))
        assertFalse(isTruncatedContent("A full paragraph."))
        assertFalse(isTruncatedContent(null))
    }
}
