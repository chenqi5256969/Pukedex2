package com.revenco.pokedex2.ui.adapter.loadmore

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class RecyclerScrollBottom constructor(private val loadListener: LoadListener) :
    RecyclerView.OnScrollListener() {

    //用来标记是否正在向上滑动
    private var isSlidingUpward = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        val manager = recyclerView.layoutManager as LinearLayoutManager
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val lastPosition =
                manager.findLastCompletelyVisibleItemPosition()

            val itemCount = manager.itemCount
            if (lastPosition == itemCount - 1 && isSlidingUpward) {
                loadListener.onLoadMore()

            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        isSlidingUpward = (dy > 0)
    }
}