package com.cleveroad.blur_tutorial.utils

import android.graphics.Point
import android.graphics.Rect
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import com.cleveroad.blur_tutorial.ext.relativeLeft
import com.cleveroad.blur_tutorial.ext.relativeTop

/**
 * @return is [this] view in visible area.
 */
internal fun View.isInVisibleArea(): Boolean =
        getLocalVisibleRect(Rect())

/**
 * If one of all parents of [this] view is [ScrollView] or [NestedScrollView] or [HorizontalScrollView],
 * it'll be scrolled to [this] view.
 *
 * @param xOffset [Int] scroll X offset
 * @param yOffset [Int] scroll Y offset
 */
internal fun View.scrollToThis(xOffset: Int = 0, yOffset: Int = 0) {
    scrollToView(this, xOffset, yOffset)
}

/**
 * Scroll [ScrollView] to [view]'s left, top position with specified [xOffset], [yOffset].
 *
 * @param view [View] that should be visible.
 * @param xOffset [Int] scroll X offset
 * @param yOffset [Int] scroll Y offset
 *
 * @return [Point] with consumed scroll positions.
 */
internal fun ScrollView.scrollToView(view: View, xOffset: Int, yOffset: Int): Point {
    val prevScrollX = scrollX
    val prevScrollY = scrollY
    scrollTo(view.relativeLeft(this) - xOffset,
            view.relativeTop(this) - yOffset)
    return Point(scrollX - prevScrollX, scrollY - prevScrollY)
}

/**
 * Scroll [NestedScrollView] to [view]'s left, top position with specified [xOffset], [yOffset].
 *
 * @param view [View] that should be visible.
 * @param xOffset [Int] scroll X offset
 * @param yOffset [Int] scroll Y offset
 *
 * @return [Point] with consumed scroll positions.
 */
internal fun NestedScrollView.scrollToView(view: View, xOffset: Int, yOffset: Int): Point {
    val prevScrollX = scrollX
    val prevScrollY = scrollY
    scrollTo((view.relativeLeft(this) - xOffset),
            (view.relativeTop(this) - yOffset))
    return Point(scrollX - prevScrollX, scrollY - prevScrollY)
}

/**
 * Scroll [HorizontalScrollView] to [view]'s left, top position with specified [xOffset], [yOffset].
 *
 * @param view [View] that should be visible.
 * @param xOffset [Int] scroll X offset
 * @param yOffset [Int] scroll Y offset
 *
 * @return [Point] with consumed scroll positions.
 */
internal fun HorizontalScrollView.scrollToView(view: View, xOffset: Int, yOffset: Int): Point {
    val prevScrollX = scrollX
    val prevScrollY = scrollY
    scrollTo(view.relativeLeft(this) - xOffset,
            view.relativeTop(this) - yOffset)
    return Point(scrollX - prevScrollX, scrollY - prevScrollY)
}

private fun View.scrollToView(view: View, xOffset: Int = 0, yOffset: Int = 0) {
    val consumedScroll = when (val parentView = parent) {
        is ScrollView -> parentView.scrollToView(view, xOffset, yOffset)
        is NestedScrollView -> parentView.scrollToView(view, xOffset, yOffset)
        is HorizontalScrollView -> parentView.scrollToView(view, xOffset, yOffset)
        else -> Point(0, 0)
    }
    (parent as? View)?.scrollToView(view,
            xOffset - consumedScroll.x,
            yOffset - consumedScroll.y)
}