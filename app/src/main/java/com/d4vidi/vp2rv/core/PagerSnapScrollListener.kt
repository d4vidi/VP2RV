package com.d4vidi.vp2rv.core

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

open class PagerSnapScrollListener(val recyclerView: RecyclerView, val externalListener: RVPagerStateListener, maxPages: Int) : RecyclerView.OnScrollListener() {
    var pageStates: MutableList<VisiblePageState> = ArrayList(maxPages)
    var pageStatesPool = List(maxPages, { _ -> VisiblePageState(0, recyclerView, 0, 0, 0f) })

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        val layoutManager = recyclerView!!.layoutManager as LinearLayoutManager

        val firstPos = layoutManager.findFirstVisibleItemPosition()
        val lastPos = layoutManager.findLastVisibleItemPosition()

        val screenEndX = recyclerView.context.resources.displayMetrics.widthPixels
        val midScreen = (screenEndX / 2)

        for (position in firstPos..lastPos) {
            val view = layoutManager.findViewByPosition(position)
            val viewWidth = view.measuredWidth
            val viewStartX = view.x
            val viewEndX = viewStartX + viewWidth
            if (viewEndX >= 0 && viewStartX <= screenEndX) {
                val viewHalfWidth = view.measuredWidth / 2f

                val pageState = pageStatesPool[position - firstPos]
                pageState.index = position
                pageState.view = view
                pageState.viewCenterX = (viewStartX + viewWidth / 2f).toInt()
                pageState.distanceToSettledPixels = (pageState.viewCenterX - midScreen)
                pageState.distanceToSettled = (pageState.viewCenterX + viewHalfWidth) / (midScreen + viewHalfWidth)
                pageStates.add(pageState)
            }
        }
        externalListener.onPageScroll(pageStates)

        // Clear this in advance so as to avoid holding refs to views.
        pageStates.clear()
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        externalListener.onScrollStateChanged(statesArray[newState])
    }

    companion object {
        val statesArray = listOf(RVPageScrollState.Idle(), RVPageScrollState.Dragging(), RVPageScrollState.Settling())
    }
}
