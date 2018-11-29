package com.d4vidi.vp2rv.core

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import android.support.v7.widget.RecyclerView
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class RVPagerSnapFancyDecoratorTest {

    private lateinit var context: Context
    private lateinit var rvAdapter: RecyclerView.Adapter<*>
    private lateinit var rv: RecyclerView
    private lateinit var outRect: Rect

    private fun givenSingleItem() {
        `when`(rvAdapter.itemCount).thenReturn(1)
        `when`(rv.getChildAdapterPosition(any(View::class.java))).thenReturn(0)
    }

    private fun givenTwoItems(view1: View, view2: View) {
        `when`(rvAdapter.itemCount).thenReturn(2)
        `when`(rv.getChildAdapterPosition(view1)).thenReturn(0)
        `when`(rv.getChildAdapterPosition(view2)).thenReturn(1)
    }

    private fun givenScreenWidth(widthPx: Int) {
        val res = mock(Resources::class.java)
        `when`(context.resources).thenReturn(res)

        val displayMetrics = mock(DisplayMetrics::class.java)
        `when`(res.displayMetrics).thenReturn(displayMetrics)

        displayMetrics.widthPixels = widthPx
    }

    @Before
    fun setUp() {
        context = mock(Context::class.java)

        rvAdapter = mock(RecyclerView.Adapter::class.java)
        rv = mock(RecyclerView::class.java)
        `when`(rv.adapter).thenReturn(rvAdapter)

        outRect = mock(Rect::class.java)
    }

    @Test
    fun shouldSetTrivialSingleItemWithoutInsets() {
        givenSingleItem()

        val uut = RVPagerSnapFancyDecorator(100, 100, 0f)
        uut.getItemOffsets(outRect, null, rv, null)

        verify(outRect).set(0, 0, 0, 0)
    }

    @Test
    fun shouldSetTrivialSingleItemInsets() {
        givenSingleItem()

        val uut = RVPagerSnapFancyDecorator(100, 80, 0f)
        uut.getItemOffsets(outRect, null, rv, null)

        verify(outRect).set(10, 0, 10, 0)
    }

    @Test
    fun shouldSetTrivialTwoItemsInsets() {
        val view1 = mock(View::class.java)
        val view2 = mock(View::class.java)

        givenTwoItems(view1, view2)

        val uut = RVPagerSnapFancyDecorator(100, 80, 0f)
        uut.getItemOffsets(outRect, view1, rv, null)
        uut.getItemOffsets(outRect, view2, rv, null)

        verify(outRect).set(10, 0, 5, 0)
        verify(outRect).set(5, 0, 10, 0)
    }

    @Test
    fun shouldSetInsetsForTwoItemsWithPeeking() {
        val view1 = mock(View::class.java)
        val view2 = mock(View::class.java)

        givenTwoItems(view1, view2)

        val uut = RVPagerSnapFancyDecorator(1000, 800, .01f)
        uut.getItemOffsets(outRect, view1, rv, null)
        uut.getItemOffsets(outRect, view2, rv, null)

        verify(outRect).set(100, 0, 50 - 8, 0)
        verify(outRect).set(50 - 8, 0, 100, 0)
    }

    @Test
    fun shouldExtractScreenWidthInDedicatedCtor() {
        val view1 = mock(View::class.java)
        val view2 = mock(View::class.java)

        givenTwoItems(view1, view2)
        givenScreenWidth(100)

        val uut = RVPagerSnapFancyDecorator(context, 80, 0f)
        uut.getItemOffsets(outRect, view1, rv, null)
        uut.getItemOffsets(outRect, view2, rv, null)

        verify(outRect).set(10, 0, 5, 0)
        verify(outRect).set(5, 0, 10, 0)
    }
}
