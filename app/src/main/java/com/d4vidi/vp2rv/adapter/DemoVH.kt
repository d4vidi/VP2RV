package com.d4vidi.vp2rv.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.support.annotation.Px
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.d4vidi.vp2rv.R
import com.d4vidi.vp2rv.customui.CircularProgressBar

class DemoVH : RecyclerView.ViewHolder, IDemoVH {
    private val mPageIndexText: TextView

    private val mPositioningText: TextView
    private val mProgressBar: CircularProgressBar
    constructor(inflater: LayoutInflater, parent: ViewGroup)
            : this(inflater.inflate(R.layout.rv_page_item_func, parent, false) as ViewGroup)

    constructor(listPageView: ViewGroup)
            : super(listPageView) {
        mPageIndexText = listPageView.findViewById(R.id.pageIndex)
        mPositioningText  = listPageView.findViewById(R.id.positioningText)
        mProgressBar = listPageView.findViewById(R.id.progressBar)
        mProgressBar.setProgressColor(Color.WHITE)
        mProgressBar.showProgressText(true)
        mProgressBar.useRoundedCorners(false)
    }

    override fun bind(model: PageModel) {
        val (bkgColor) = model
        itemView.setBackgroundColor(bkgColor)
    }

    override fun setRealtimeAttr(index: Int, centerXText: String, horizOffsetRate: Float, @Px horizOffsetPx: Int) {
        mPageIndexText.text = "Page #${index}"
        mPositioningText.text = "${centerXText}px (${horizOffsetPx}px)"

        val color = if (horizOffsetRate > 1) Color.LTGRAY else Color.WHITE
        mProgressBar.setProgressColor(color)
        mProgressBar.setTextColor(color)
        mProgressBar.setProgress((horizOffsetRate * 100).toInt())
    }

    override fun onSelected() {
        val scaleXAnim = ObjectAnimator.ofFloat(mPageIndexText, View.SCALE_X, 0.75f, 1.8f, 1f, 0.8f, 1f)
        val scaleYAnim = ObjectAnimator.ofFloat(mPageIndexText, View.SCALE_Y, 0.75f, 1.8f, 1f, 0.8f, 1f)


        val animation = AnimatorSet()
        animation.playTogether(scaleXAnim, scaleYAnim)
        animation.duration = 600

        animation.start()
    }
}