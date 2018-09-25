package com.d4vidi.vp2rv.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

data class PageModel(val bkgColor: Int)

open class RichDemoRVAdapter(private val mModels: List<PageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mInflater: LayoutInflater? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mInflater = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DemoVH(mInflater!!, parent)
    }

    override fun getItemCount() = mModels.size
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = mModels[position]
        (holder as IDemoVH).bind(model)
    }
}
