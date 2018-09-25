package com.d4vidi.vp2rv.core

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver

open class PagerSnapHelperVerbose(protected val recyclerView: RecyclerView, val externalListener: RVPagerStateListener)
    : PagerSnapHelper()
    , ViewTreeObserver.OnGlobalLayoutListener {

    var lastPage = RecyclerView.NO_POSITION

    init {
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        val position = (recyclerView.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
        if (position != RecyclerView.NO_POSITION) {
            notifyNewPageIfNeeded(position)
            recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        val view = super.findSnapView(layoutManager)
        notifyNewPageIfNeeded(recyclerView.getChildAdapterPosition(view))
        return view
    }

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager?, velocityX: Int, velocityY: Int): Int {
        val position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)

        if (position < recyclerView.adapter.itemCount) { // Making up for a "bug" in the original snap-helper.
            notifyNewPageIfNeeded(position)
        }
        return position
    }

    protected fun notifyNewPageIfNeeded(page: Int) {
        if (page != lastPage) {
            this.externalListener.onPageSelected(page)
            lastPage = page
        }
    }
}
