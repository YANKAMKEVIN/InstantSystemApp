package com.kev.instantsystem.data.di

import com.kev.instantsystem.data.search.RecentSearchStorage
import com.kev.instantsystem.data.search.SharedPreferencesRecentSearchStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RecentSearchModule {
    @Binds
    abstract fun bindRecentSearchStorage(storage: SharedPreferencesRecentSearchStorage): RecentSearchStorage
}
