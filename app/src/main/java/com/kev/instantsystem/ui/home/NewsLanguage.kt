package com.kev.instantsystem.ui.home

import java.util.Locale

/** Languages accepted by the `language` parameter of NewsAPI `/v2/everything`. */
val NEWS_API_LANGUAGES = setOf(
    "ar", "de", "en", "es", "fr", "he", "it", "nl", "no", "pt", "ru", "sv", "ud", "zh"
)

/** Used when the device language is not one NewsAPI can filter on. */
const val DEFAULT_NEWS_LANGUAGE = "en"

/**
 * NewsAPI language code for a device language code ("fr", "fr-FR", "iw"...).
 * Maps the codes that differ from NewsAPI (legacy Hebrew "iw", Urdu "ur" -> "ud", Norwegian
 * Bokmål / Nynorsk -> "no") and falls back to [DEFAULT_NEWS_LANGUAGE] for the others.
 */
fun newsApiLanguage(languageCode: String): String {
    val code = languageCode.substringBefore('-').substringBefore('_').lowercase(Locale.ROOT)
    val mapped = when (code) {
        "iw" -> "he"
        "ur" -> "ud"
        "nb", "nn" -> "no"
        else -> code
    }
    return if (mapped in NEWS_API_LANGUAGES) mapped else DEFAULT_NEWS_LANGUAGE
}
