package com.kev.instantsystem.data.di

import com.kev.instantsystem.data.paging.EverythingPagingSourceFactory
import com.kev.instantsystem.data.paging.NewsPagingSourceFactory
import com.kev.instantsystem.data.remote.NewsApiService
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSource
import com.kev.instantsystem.data.remote.datasource.NewsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {
    @Provides
    @Singleton
    fun provideNewsRemoteDataSource(newsApiService: NewsApiService): NewsRemoteDataSource {
        return NewsRemoteDataSourceImpl(newsApiService)
    }

    @Provides
    @Singleton
    fun provideNewsPagingSourceFactory(
        newsRemoteDataSource: NewsRemoteDataSource
    ): NewsPagingSourceFactory {
        return NewsPagingSourceFactory(newsRemoteDataSource)
    }

    @Provides
    @Singleton
    fun provideEverythingPagingSourceFactory(
        newsRemoteDataSource: NewsRemoteDataSource
    ): EverythingPagingSourceFactory {
        return EverythingPagingSourceFactory(newsRemoteDataSource)
    }

}