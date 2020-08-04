package com.humansuit.foody.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewPaginator(private val layoutManager: LinearLayoutManager)
    : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()


        if (canLoadMore()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && !recyclerView.canScrollHorizontally(1))
            {
                loadMoreItems()
            }
        }
    }

    private fun canLoadMore() = !isLoading() && !isLastPage()

    protected abstract fun loadMoreItems()
    abstract fun isLastPage() : Boolean
    abstract fun isLoading() :Boolean

}