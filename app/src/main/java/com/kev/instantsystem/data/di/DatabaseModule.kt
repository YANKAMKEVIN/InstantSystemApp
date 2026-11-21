package com.kev.instantsystem.data.di

import android.content.Context
import androidx.room.Room
import com.kev.instantsystem.data.local.HeadlinesDao
import com.kev.instantsystem.data.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(context, NewsDatabase::class.java, NewsDatabase.NAME).build()

    @Provides
    fun provideHeadlinesDao(database: NewsDatabase): HeadlinesDao = database.headlinesDao()
}
