package com.kev.instantsystem.data.di

import com.kev.instantsystem.data.local.HeadlinesDao
import com.kev.instantsystem.data.paging.EverythingPagingSourceFactory
import com.kev.instantsystem.data.paging.NewsPagingSourceFactory
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.data.repository.NewsRepositoryImpl
import com.kev.instantsystem.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun provideNewsRepository(
        newsPagingSourceFactory: NewsPagingSourceFactory,
        everythingPagingSourceFactory: EverythingPagingSourceFactory,
        newsRemoteDataSource: NewsRemoteDataSource,
        headlinesDao: HeadlinesDao
    ): NewsRepository {
        return NewsRepositoryImpl(
            headlinesPagingSourceFactory = newsPagingSourceFactory,
            everythingPagingSourceFactory = everythingPagingSourceFactory,
            remoteDataSource = newsRemoteDataSource,
            headlinesDao = headlinesDao
        )
    }
}