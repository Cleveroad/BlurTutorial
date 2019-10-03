package com.cleveroad.blur_tutorial.ext

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import com.cleveroad.blur_tutorial.utils.HALF_DIVIDER

private const val X_INDEX = 0
private const val Y_INDEX = 1

internal val View.relativeHeight
    get() = height.takeIf { it < screenHeight } ?: screenHeight

internal val View.relativeWidth
    get() = width.takeIf { it < screenWidth } ?: screenWidth

internal val View.hitRect
    get() = Rect().also { getHitRect(it) }

internal val View.drawingRect
    get() = Rect().also { getDrawingRect(it) }

internal val View.screenHeight
    get() = context?.resources?.displayMetrics?.heightPixels ?: 0

internal val View.screenWidth
    get() = context?.resources?.displayMetrics?.widthPixels ?: 0

internal val View.locationOnScreen
    get() = intArrayOf(0, 0).also { getLocationOnScreen(it) }

/**
 * Relative Y coordinate of [View]'s top.
 */
internal val View.topLocationOnScreen
    get() = locationOnScreen[Y_INDEX].takeIf { it >= 0 } ?: 0

/**
 * Relative Y coordinate of [View]'s bottom.
 */
internal val View.bottomLocationOnScreen
    get() = topLocationOnScreen + relativeHeight

/**
 * Relative Y coordinate of [View]'s left.
 */
internal val View.leftLocationOnScreen
    get() = locationOnScreen[X_INDEX].takeIf { it >= 0 } ?: 0

/**
 * Relative Y coordinate of [View]'s right.
 */
internal val View.rightLocationOnScreen
    get() = leftLocationOnScreen + relativeWidth

internal val View.locationOnScreenRect
    get() = Rect(leftLocationOnScreen, topLocationOnScreen, rightLocationOnScreen, bottomLocationOnScreen)

/**
 * Y coordinate of [View]'s top.
 */
internal val View.topLocationInWindow
    get() = locationOnScreen[Y_INDEX]

/**
 * Y coordinate of [View]'s bottom
 */
internal val View.bottomLocationInWindow
    get() = topLocationInWindow + height

/**
 * Y coordinate of [View]'s left.
 */
internal val View.leftLocationInWindow
    get() = locationOnScreen[X_INDEX]

/**
 * Y coordinate of [View]'s right.
 */
internal val View.rightLocationInWindow
    get() = leftLocationOnScreen + width

/**
 * Relative coordinates of [View]'s x center.
 */
internal val View.xCenter
    get() = (leftLocationOnScreen + rightLocationOnScreen) / HALF_DIVIDER

/**
 * Relative coordinates of [View]'s y center.
 */
internal val View.yCenter
    get() = (topLocationOnScreen + bottomLocationOnScreen) / HALF_DIVIDER

/**
 * Get Bitmap from [this] View.
 *
 * @param config [Bitmap.Config] bitmap configuration
 *
 * @return [Bitmap] bitmap with view screenshot
 */
internal fun View.getBitmap(config: Bitmap.Config = ARGB_8888) = takeIf { hasSize() }?.run {
    val bmp = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(bmp)
    draw(canvas)
    return@run bmp
}

/**
 * Get left position, relative to [parent]
 *
 * @param parent [View] one of parents of [this] view
 *
 * @return [Int] left position, relative to [parent]
 */
internal fun View.relativeLeft(parent: View): Int =
        (this.parent as? View)?.takeUnless { it == parent }?.let { viewParent ->
            left + viewParent.relativeLeft(parent)
        } ?: 0

/**
 * Get top position, relative to [parent]
 *
 * @param parent [View] one of parents of [this] view
 *
 * @return [Int] top position, relative to [parent]
 */
internal fun View.relativeTop(parent: View): Int =
        (this.parent as? View)?.takeUnless { it == parent }?.let { viewParent ->
            top + viewParent.relativeTop(parent)
        } ?: top

/**
 * Get right position, relative to [parent]
 *
 * @param parent [View] one of parents of [this] view
 *
 * @return [Int] right position, relative to [parent]
 */
internal fun View.relativeRight(parent: View): Int = relativeLeft(parent) + width

/**
 * Get bottom position, relative to [parent]
 *
 * @param parent [View] one of parents of [this] view
 *
 * @return [Int] bottom position, relative to [parent]
 */
internal fun View.relativeBottom(parent: View): Int = relativeTop(parent) + height

/**
 * @return [View] root view of layout
 */
internal fun View.getRoot(): View? = (parent as? View)?.getRoot() ?: this

internal fun View.hide(gone: Boolean = true) {
    visibility = if (gone) View.GONE else View.INVISIBLE
}

internal fun View.show() {
    visibility = View.VISIBLE
}

private fun View.hasSize() = width > 0 && height > 0