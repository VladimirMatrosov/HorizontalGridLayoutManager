package com.food.automativelayoutmanager

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

class HorizontalGridLayoutManager(
    private val columnCount: Int,
    private val rowCount: Int,
    private val reverseLayout: Boolean
) : RecyclerView.LayoutManager() {

    private val widthByColumns
        get() = width / columnCount

    private val heightByRows
        get() = height / rowCount

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler?,
        state: RecyclerView.State?
    ): Int {
        val delta = if (reverseLayout) {
            scrollHorizontallyReverseInternal(dx)
        } else {
            scrollHorizontallyInternal(dx)
        }

        offsetChildrenHorizontal(-delta)
        return delta
    }

    private fun scrollHorizontallyInternal(dx: Int): Int {
        val childCount = childCount
        val itemCount = itemCount
        if (childCount == 0) {
            return 0
        }
        val startView = getChildAt(0) ?: return 0
        val endView = getChildAt(childCount - 1) ?: return 0

        val viewSpan = getDecoratedRight(endView) - getDecoratedLeft(startView)
        if (viewSpan <= width) {
            return 0
        }

        if (dx < 0) {
            val firstView = getChildAt(0) ?: return 0
            val firstViewAdapterPos = getPosition(firstView)
            return if (firstViewAdapterPos > 0) {
                dx
            } else {
                val viewLeft = getDecoratedLeft(firstView)
                max(viewLeft, dx)
            }
        } else if (dx > 0) {
            val lastView = getChildAt(childCount - 1) ?: return 0
            val lastViewAdapterPos = getPosition(lastView)
            return if (lastViewAdapterPos < itemCount - 1) {
                dx
            } else {
                val viewRight = getDecoratedRight(lastView)
                val parentRight = width
                min(viewRight - parentRight, dx)
            }
        }

        return 0
    }

    private fun scrollHorizontallyReverseInternal(dx: Int): Int {
        val childCount = childCount
        val itemCount = itemCount
        if (childCount == 0) {
            return 0
        }
        val startView = getChildAt(0) ?: return 0
        val endView = getChildAt(childCount - 1) ?: return 0

        val viewSpan = getDecoratedRight(startView) - getDecoratedLeft(endView)
        if (viewSpan <= width) {
            return 0
        }

        if (dx < 0) {
            val lastView = getChildAt(childCount - 1) ?: return 0
            val lastViewAdapterPos = getPosition(lastView)
            return if (lastViewAdapterPos < itemCount - 1) {
                dx
            } else {
                val viewLeft = getDecoratedLeft(lastView)
                max(viewLeft, dx)
            }
        } else if (dx > 0) {
            val firstView = getChildAt(0) ?: return 0
            val firstViewAdapterPos = getPosition(firstView)
            return if (firstViewAdapterPos > 0) {
                dx
            } else {
                val viewRight = getDecoratedRight(firstView)
                val parentRight = width
                min(viewRight - parentRight, dx)
            }
        }

        return 0
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.MATCH_PARENT
        )
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)

        for (position in 0 until itemCount) {
            if (reverseLayout) {
                onLayoutChildrenReverseInPosition(position, recycler)
            } else {
                onLayoutChildrenInPosition(position, recycler)
            }
        }
    }

    private fun onLayoutChildrenInPosition(position: Int, recycler: RecyclerView.Recycler) {
        val view = recycler.getViewForPosition(position)
        val viewLeft = getViewLeftByPosition(position)
        val viewTop = getViewTopByPosition(position)

        addView(view)
        val widthSpec =
            View.MeasureSpec.makeMeasureSpec(widthByColumns, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(heightByRows, View.MeasureSpec.EXACTLY)
        measureChildWithDecorationsAndMargin(view, widthSpec, heightSpec)

        layoutDecorated(
            view,
            viewLeft,
            viewTop,
            widthByColumns + viewLeft,
            heightByRows + viewTop
        )
    }

    private fun onLayoutChildrenReverseInPosition(position: Int, recycler: RecyclerView.Recycler) {
        val view = recycler.getViewForPosition(position)
        val viewRight = getViewRightByPosition(position)
        val viewTop = getViewTopByPosition(position)

        addView(view)
        val widthSpec =
            View.MeasureSpec.makeMeasureSpec(widthByColumns, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(heightByRows, View.MeasureSpec.EXACTLY)
        measureChildWithDecorationsAndMargin(view, widthSpec, heightSpec)

        layoutDecorated(
            view,
            viewRight - widthByColumns,
            viewTop,
            viewRight,
            heightByRows + viewTop
        )
    }

    // get view left for not-reverse layout
    private fun getViewLeftByPosition(position: Int): Int {
        val onePageSize = columnCount * rowCount
        val pageNumber = position / onePageSize
        val columnNumber = position % columnCount

        return pageNumber * width + widthByColumns * columnNumber
    }

    // get view right for reverse layout
    private fun getViewRightByPosition(position: Int): Int {
        val onePageSize = columnCount * rowCount
        val pageNumber = position / onePageSize
        val columnNumber = position % columnCount

        return width - widthByColumns * columnNumber - pageNumber * width
    }

    private fun getViewTopByPosition(position: Int): Int {
        val onePageSize = columnCount * rowCount
        val inPageNumber = position % onePageSize
        val rowNumber = inPageNumber / columnCount

        return heightByRows * rowNumber
    }

    private fun measureChildWithDecorationsAndMargin(child: View, widthSpec: Int, heightSpec: Int) {
        val decorRect = Rect()
        calculateItemDecorationsForChild(child, decorRect)
        val lp = child.layoutParams as RecyclerView.LayoutParams

        val updatedWidthSpec = updateSpecWithExtra(
            widthSpec,
            lp.leftMargin + decorRect.left,
            lp.rightMargin + decorRect.right
        )

        val updatesHeightSpec = updateSpecWithExtra(
            heightSpec,
            lp.topMargin + decorRect.top,
            lp.bottomMargin + decorRect.bottom
        )

        child.measure(updatedWidthSpec, updatesHeightSpec)
    }

    private fun updateSpecWithExtra(spec: Int, startInset: Int, endInset: Int): Int {
        if (startInset == 0 && endInset == 0) {
            return spec
        }

        val mode = View.MeasureSpec.getMode(spec)
        return if (mode == View.MeasureSpec.AT_MOST || mode == View.MeasureSpec.EXACTLY) {
            View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.getSize(spec) - startInset - endInset,
                mode
            )
        } else {
            spec
        }
    }

}