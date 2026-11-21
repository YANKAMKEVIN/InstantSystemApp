package com.kev.instantsystem.testutils

import com.kev.instantsystem.data.search.RecentSearchStorage

class InMemoryRecentSearchStorage(var value: String? = null) : RecentSearchStorage {
    override fun read(): String? = value
    override fun write(value: String) {
        this.value = value
    }
}
