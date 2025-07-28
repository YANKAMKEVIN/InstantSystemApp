package com.kev.instantsystem.testutils

import androidx.recyclerview.widget.DiffUtil
import com.kev.instantsystem.domain.model.Article

/**
 * DiffUtil callback for testing PagingData snapshots.
 * This compares articles based on their title (you can adapt as needed).
 */
class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
