package com.kev.instantsystem.ui.components

import androidx.paging.LoadState
import org.junit.Assert.assertEquals
import org.junit.Test

class ListContentTest {

    private val loading = LoadState.Loading
    private val error = LoadState.Error(RuntimeException("HTTP 500"))
    private val done = LoadState.NotLoading(endOfPaginationReached = false)

    @Test
    fun `an empty list shows the skeleton, the error or the empty state`() {
        assertEquals(ListContent.Skeleton, resolveListContent(loading, itemCount = 0))
        assertEquals(ListContent.Error, resolveListContent(error, itemCount = 0))
        assertEquals(ListContent.Empty, resolveListContent(done, itemCount = 0))
    }

    @Test
    fun `articles stay on screen while refreshing or after a failed refresh`() {
        assertEquals(ListContent.Articles, resolveListContent(loading, itemCount = 12))
        assertEquals(ListContent.Articles, resolveListContent(error, itemCount = 12))
        assertEquals(ListContent.Articles, resolveListContent(done, itemCount = 12))
    }
}
