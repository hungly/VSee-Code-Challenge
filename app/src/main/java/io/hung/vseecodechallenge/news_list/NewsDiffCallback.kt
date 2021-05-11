package io.hung.vseecodechallenge.news_list

import androidx.recyclerview.widget.DiffUtil
import io.hung.vseecodechallenge.model.News

class NewsDiffCallback(
    private val oldNewsList: List<News>,
    private val newNewsList: List<News>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldNewsList.size

    override fun getNewListSize(): Int = newNewsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldNewsList[oldItemPosition] != newNewsList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldNewsList[oldItemPosition] != newNewsList[newItemPosition]
}