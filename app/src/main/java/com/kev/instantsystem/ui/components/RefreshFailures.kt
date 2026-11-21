package com.kev.instantsystem.ui.components

import androidx.annotation.StringRes
import androidx.paging.LoadState
import com.kev.instantsystem.R
import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** A reload that failed while the articles stayed on screen. [listKey] says which list (one per tab). */
data class RefreshFailure(val listKey: String, val error: Throwable)

/**
 * Tells the user, with a message at the bottom and a "Retry" action, that a list on screen could
 * not be reloaded. Implemented by the ViewModels of the paged lists (by delegation to [RefreshFailures]),
 * so a message is shown once, even after a rotation or a tab switch.
 */
interface RefreshFailureReporter {
    /** The failure to show now, or null. */
    val refreshFailure: StateFlow<RefreshFailure?>

    /** Called with each new refresh state of a list; only a failure with articles on screen is reported. */
    fun onListLoadState(listKey: String, refresh: LoadState, itemCount: Int)

    /** The message for [failure] has been shown (or dismissed): do not show it again. */
    fun onRefreshFailureShown(failure: RefreshFailure)
}

class RefreshFailures : RefreshFailureReporter {
    private val _refreshFailure = MutableStateFlow<RefreshFailure?>(null)
    override val refreshFailure: StateFlow<RefreshFailure?> = _refreshFailure.asStateFlow()

    /** Last error reported for each list: the same failed state seen again is not a new failure. */
    private val reported = mutableMapOf<String, Throwable>()

    override fun onListLoadState(listKey: String, refresh: LoadState, itemCount: Int) {
        if (refresh !is LoadState.Error || itemCount == 0) return
        if (reported[listKey] === refresh.error) return
        reported[listKey] = refresh.error
        _refreshFailure.value = RefreshFailure(listKey, refresh.error)
    }

    override fun onRefreshFailureShown(failure: RefreshFailure) {
        _refreshFailure.compareAndSet(failure, null)
    }
}

/** Short message of the bottom message shown when a list on screen could not be reloaded. */
@StringRes
fun refreshErrorMessageRes(error: Throwable?): Int = when ((error as? NetworkException)?.error) {
    is NetworkError.UnResolveAddress -> R.string.refresh_error_offline
    is NetworkError.TooManyRequests -> R.string.refresh_error_rate_limited
    else -> R.string.refresh_error
}
