package com.d4vidi.vp2rv.adapter

import android.support.annotation.Px

interface IDemoVH {
    fun bind(model: PageModel)
    fun setRealtimeAttr(index: Int, centerXText: String, horizOffsetRate: Float, @Px horizOffsetPx: Int);
    fun onSelected()
}