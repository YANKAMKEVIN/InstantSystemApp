package com.kev.instantsystem.ui.components

import androidx.annotation.StringRes
import com.kev.instantsystem.R
import com.kev.instantsystem.data.network.NetworkError
import com.kev.instantsystem.data.network.NetworkException

/** Message shown for a failed article list, in the interface language. */
@StringRes
fun errorMessageRes(error: Throwable?): Int = when ((error as? NetworkException)?.error) {
    is NetworkError.MissingApiKey -> R.string.error_missing_api_key
    is NetworkError.Unauthorized -> R.string.error_invalid_api_key
    is NetworkError.TooManyRequests -> R.string.error_rate_limited
    is NetworkError.UnResolveAddress -> R.string.error_offline
    else -> R.string.error_message
}
