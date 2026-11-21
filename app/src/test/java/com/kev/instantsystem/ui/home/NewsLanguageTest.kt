package com.kev.instantsystem.ui.home

import org.junit.Assert.assertEquals
import org.junit.Test

class NewsLanguageTest {

    @Test
    fun `supported languages are kept`() {
        assertEquals("fr", newsApiLanguage("fr"))
        assertEquals("zh", newsApiLanguage("zh"))
        assertEquals("pt", newsApiLanguage("PT"))
    }

    @Test
    fun `region suffixes are ignored`() {
        assertEquals("fr", newsApiLanguage("fr-CA"))
        assertEquals("en", newsApiLanguage("en_GB"))
    }

    @Test
    fun `codes that differ from NewsAPI are mapped`() {
        assertEquals("he", newsApiLanguage("iw"))
        assertEquals("ud", newsApiLanguage("ur"))
        assertEquals("no", newsApiLanguage("nb"))
        assertEquals("no", newsApiLanguage("nn"))
    }

    @Test
    fun `unsupported or empty codes fall back to English`() {
        assertEquals(DEFAULT_NEWS_LANGUAGE, newsApiLanguage("ja"))
        assertEquals(DEFAULT_NEWS_LANGUAGE, newsApiLanguage(""))
    }
}
