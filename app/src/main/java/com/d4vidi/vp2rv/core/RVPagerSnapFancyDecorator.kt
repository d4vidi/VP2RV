package com.d4vidi.vp2rv.core

import android.content.Context
import android.graphics.Rect
import android.support.annotation.Px
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * A [RecyclerView decorator][RecyclerView.ItemDecoration] which, if applied over a recycler view
 * with a [PagerSnapHelper][android.support.v7.widget.PagerSnapHelper] set up (i.e. that behaves like
 * a full-screen [ViewPager][android.support.v4.view.ViewPager]), allows for easily setting up gaps
 * between the RV's items as typically done in a cards carousel, all-the-while keeping the items
 * properly snapped to the screen's center.
 *
 * **Typical usage:**
 *
 * ```
 * // Get each item to take exactly 80% of the screen's width.
 * // In addition, enable a sneak-preview of neighbouring (unsnapped) items of 1% of their width.
 * val cardWidthPixels = activity.resources.displayMetrics.widthPixels * 0.80f
 * recyclerView.addItemDecoration(RVPagerSnapFancyDecorator(activity, cardWidthPixels, 0.01f))
 * ```
 */
open class RVPagerSnapFancyDecorator : RecyclerView.ItemDecoration {
    private val mInterItemsGap: Int
    private val mNetOneSidedGap: Int

    constructor(context: Context, @Px itemWidth: Int, itemPeekingPercent: Float = .035f)
        : this(context.resources.displayMetrics.widthPixels, itemWidth, itemPeekingPercent)

    constructor(@Px totalWidth: Int, @Px itemWidth: Int, itemPeekingPercent: Float = .035f) {
        val cardPeekingWidth = (itemWidth * itemPeekingPercent + .5f).toInt()

        mInterItemsGap = (totalWidth - itemWidth) / 2
        mNetOneSidedGap = mInterItemsGap / 2 - cardPeekingWidth
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, recyclerView: RecyclerView?, state: RecyclerView.State?) {
        val index = recyclerView!!.getChildAdapterPosition(view)
        val isFirstItem = isFirstItem(index)
        val isLastItem = isLastItem(index, recyclerView)

        val leftInset = if (isFirstItem) mInterItemsGap else mNetOneSidedGap
        val rightInset = if (isLastItem) mInterItemsGap else mNetOneSidedGap

        outRect!!.set(leftInset, 0, rightInset, 0)
    }

    private fun isFirstItem(index: Int) = index == 0
    private fun isLastItem(index: Int, recyclerView: RecyclerView) = index == recyclerView.adapter.itemCount - 1
}
