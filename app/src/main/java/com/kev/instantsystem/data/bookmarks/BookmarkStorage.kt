package com.kev.instantsystem.data.bookmarks

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/** Raw persistence of the saved articles, kept behind an interface so the logic can be tested on the JVM. */
interface BookmarkStorage {
    fun read(): String?
    fun write(value: String)
}

class SharedPreferencesBookmarkStorage @Inject constructor(
    @ApplicationContext context: Context
) : BookmarkStorage {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun read(): String? = preferences.getString(KEY_BOOKMARKS, null)

    override fun write(value: String) {
        preferences.edit().putString(KEY_BOOKMARKS, value).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "bookmarks"
        const val KEY_BOOKMARKS = "saved_articles"
    }
}
