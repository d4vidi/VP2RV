package com.d4vidi.vp2rv

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.d4vidi.vp2rv.adapter.IDemoVH
import com.d4vidi.vp2rv.adapter.PageModel
import com.d4vidi.vp2rv.adapter.RichDemoRVAdapter
import com.d4vidi.vp2rv.core.RVPageScrollState
import com.d4vidi.vp2rv.core.RVPagerSnapHelperListenable
import com.d4vidi.vp2rv.core.RVPagerStateListener
import com.d4vidi.vp2rv.core.VisiblePageState

class RVFragment : Fragment() {
    private var mActivity: AppCompatActivity? = null

    private val mModels = listOf(
            PageModel(Color.rgb(0xFD, 0xBC, 0x5F)),
            PageModel(Color.rgb(242, 101, 49)),
            PageModel(Color.rgb(233, 119, 175)),
            PageModel(Color.rgb(117, 129, 191)),
            PageModel(Color.rgb(114, 204, 210)),
            PageModel(Color.rgb(200, 223, 142))
    )

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val rv = root.findViewById(R.id.recycler_view) as RecyclerView
        rv.layoutManager = LinearLayoutManager(container!!.context, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = RichDemoRVAdapter(mModels)

        RVPagerSnapHelperListenable().attachToRecyclerView(rv, object : RVPagerStateListener {
            override fun onPageScroll(pagesState: List<VisiblePageState>) {
                for (pageState in pagesState) {
                    val vh = rv.findContainingViewHolder(pageState.view) as IDemoVH
                    vh.setRealtimeAttr(pageState.index, pageState.viewCenterX.toString(), pageState.distanceToSettled, pageState.distanceToSettledPixels)
                }
            }

            override fun onScrollStateChanged(state: RVPageScrollState) {
            }

            override fun onPageSelected(index: Int) {
                val vh = rv.findViewHolderForAdapterPosition(index) as IDemoVH?
                vh?.onSelected()
            }
        })

        val toolbar = mActivity!!.findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "RecyclerViewPager Demo"

        return root
    }
}
