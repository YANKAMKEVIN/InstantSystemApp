package com.kev.instantsystem.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Local database, used for the offline cache of the top headlines.
 * Version 1: no database existed before, so there is no migration yet. The schema of each
 * version is exported to app/schemas so later migrations can be written and tested.
 */
@Database(
    entities = [HeadlineArticleEntity::class, HeadlineListEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun headlinesDao(): HeadlinesDao

    companion object {
        const val NAME = "news.db"
    }
}
