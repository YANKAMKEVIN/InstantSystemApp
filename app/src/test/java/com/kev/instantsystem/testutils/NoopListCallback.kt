package com.kev.instantsystem.testutils

import androidx.recyclerview.widget.ListUpdateCallback

/**
 * A no-op implementation of ListUpdateCallback for use with AsyncPagingDataDiffer in tests.
 */
class NoopListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) = Unit
    override fun onRemoved(position: Int, count: Int) = Unit
    override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
    override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
}
