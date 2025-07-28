package com.kev.instantsystem.testutils

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.kev.instantsystem.domain.model.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle

/**
 * Helper function to collect PagingData items using AsyncPagingDataDiffer.
 *
 * @param dispatcher The dispatcher to run the differ on (usually a test dispatcher).
 * @param scope The test scope to call advanceUntilIdle or runCurrent if needed.
 */
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun collectPagingData(
    flow: Flow<PagingData<Article>>,
    dispatcher: CoroutineDispatcher,
    scope: TestScope
): List<Article> {
    val differ = AsyncPagingDataDiffer(
        diffCallback = ArticleDiffCallback(),
        updateCallback = NoopListCallback(),
        workerDispatcher = dispatcher
    )
    val pagingData = flow.first()
    differ.submitData(pagingData)


    scope.advanceUntilIdle()
    return differ.snapshot().items
}
