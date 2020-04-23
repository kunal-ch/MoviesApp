package com.kc.movies.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class EndlessRecyclerViewScrollListener(): RecyclerView.OnScrollListener() {

    private var visibleThreshold = 1
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = 0

    var mLayoutManager: RecyclerView.LayoutManager? = null

    constructor(layoutManager: LinearLayoutManager?): this() {
        mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager): this() {
        mLayoutManager = layoutManager
        visibleThreshold = visibleThreshold * layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager): this() {
        mLayoutManager = layoutManager
        visibleThreshold = visibleThreshold * layoutManager.spanCount
    }

    open fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (!Search.isSearching) {
            var lastVisibleItemPosition = 0
            val totalItemCount = mLayoutManager!!.itemCount
            if (mLayoutManager is StaggeredGridLayoutManager) {
                val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                // get maximum element within the list
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            } else if (mLayoutManager is GridLayoutManager) {
                lastVisibleItemPosition = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            } else if (mLayoutManager is LinearLayoutManager) {
                lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                currentPage = startingPageIndex
                previousTotalItemCount = totalItemCount
                if (totalItemCount == 0) {
                    loading = true
                }
            }
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && totalItemCount > previousTotalItemCount) {
                loading = false
                previousTotalItemCount = totalItemCount
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            // threshold should reflect how many total columns there are too
            if (!loading && lastVisibleItemPosition + visibleThreshold > totalItemCount) {
                currentPage++
                onLoadMore(currentPage, totalItemCount, recyclerView)
                loading = true
            }
        }
    }

    open fun resetState() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
        loading = true
    }

    // Defines the process for actually loading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?)
}