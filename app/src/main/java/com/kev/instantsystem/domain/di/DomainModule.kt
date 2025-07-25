package com.kev.instantsystem.domain.di

import com.kev.instantsystem.domain.repository.NewsRepository
import com.kev.instantsystem.domain.usecase.GetTopHeadlinesUseCase
import com.kev.instantsystem.domain.usecase.SearchEverythingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Singleton
    @Provides
    fun provideGetTopHeadlinesUseCase(repository: NewsRepository): GetTopHeadlinesUseCase =
        GetTopHeadlinesUseCase(repository)

    @Singleton
    @Provides
    fun provideSearchEverythingUseCase(repository: NewsRepository): SearchEverythingUseCase =
        SearchEverythingUseCase(repository)

}