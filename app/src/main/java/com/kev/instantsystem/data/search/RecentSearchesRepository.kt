package com.kev.instantsystem.data.search

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Raw persistence of the recent searches, kept behind an interface so the logic can be tested on the JVM. */
interface RecentSearchStorage {
    fun read(): String?
    fun write(value: String)
}

class SharedPreferencesRecentSearchStorage @Inject constructor(
    @ApplicationContext context: Context
) : RecentSearchStorage {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override fun read(): String? = preferences.getString(KEY_RECENT_SEARCHES, null)

    override fun write(value: String) {
        preferences.edit().putString(KEY_RECENT_SEARCHES, value).apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "search"
        const val KEY_RECENT_SEARCHES = "recent_searches"
    }
}

/**
 * The latest searches (newest first), kept on the device from one launch of the app to the next,
 * like the bookmarks (SharedPreferences, JSON via Gson).
 */
@Singleton
class RecentSearchesRepository @Inject constructor(
    private val storage: RecentSearchStorage
) {
    private val gson = Gson()

    fun load(): List<String> = try {
        storage.read()
            ?.let { gson.fromJson(it, Array<String>::class.java) }
            ?.filter { it.isNotBlank() }
            ?.take(MAX_RECENT_SEARCHES)
            .orEmpty()
    } catch (e: Exception) {
        emptyList()
    }

    fun save(searches: List<String>) {
        storage.write(gson.toJson(searches.take(MAX_RECENT_SEARCHES).toTypedArray()))
    }

    companion object {
        const val MAX_RECENT_SEARCHES = 5
    }
}
