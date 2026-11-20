package com.kev.instantsystem.data.di

import com.kev.instantsystem.data.bookmarks.BookmarkStorage
import com.kev.instantsystem.data.bookmarks.SharedPreferencesBookmarkStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkModule {
    @Binds
    abstract fun bindBookmarkStorage(storage: SharedPreferencesBookmarkStorage): BookmarkStorage
}
