package com.cleveroad.blur_tutorial.explanator

import android.graphics.Path
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cleveroad.blur_tutorial.explanator.base.BaseExplanator
import com.cleveroad.blur_tutorial.explanator.extract.MenuViewExtractor
import com.cleveroad.blur_tutorial.explanator.highlight.HighlightCallback
import com.cleveroad.blur_tutorial.explanator.highlight.Highlighter
import com.cleveroad.blur_tutorial.explanator.popup.DynamicPopupWindow
import com.cleveroad.blur_tutorial.ext.*
import com.cleveroad.blur_tutorial.state.TutorialConfig
import com.cleveroad.blur_tutorial.state.TutorialConfig.Companion.NO_LAYOUT
import com.cleveroad.blur_tutorial.state.tutorial.*
import com.cleveroad.blur_tutorial.utils.HALF_DIVIDER
import com.cleveroad.blur_tutorial.utils.isInVisibleArea
import com.cleveroad.blur_tutorial.utils.scrollToThis

internal class ExplanatorImpl(config: TutorialConfig,
                              callback: ExplanatorCallback) : BaseExplanator(config, callback) {

    private val highlightCallback = object : HighlightCallback {

        override fun onHighlight(drawable: Drawable) {
            foreground = drawable
        }
    }

    private val highlighter = Highlighter(highlightCallback)

    private var popup: DynamicPopupWindow? = null

    override fun explain(state: TutorialState) {
        when (state) {
            is ViewState -> state.run {
                explainView(view, popupLayout)
            }
            is MenuState -> state.run {
                explainMenuItem(menuOwner, menuItemId, popupLayout)
            }
            is RecyclerItemState -> state.run {
                explainRecyclerItem(recyclerView, itemPosition, popupLayout)
            }
            is PathState -> state.run {
                explainPath(path, parent, popupLayout)
            }
        }
    }

    override fun cancel() {
        highlighter.release()
        foreground = null
        hideCurtain()
        popup?.dismiss()
        invalidate()
    }

    override fun onDestroy() {
        cancel()
    }

    /**
     * Highlight the [view].
     * Also, show the popup with some explanation.
     *
     * @param view [View] to explain
     * @param popupLayout layout of popup specified for concrete state.
     * If not specified, will be used default layout of popup.
     */
    private fun explainView(view: View, @LayoutRes popupLayout: Int?) {
        if (checkHighlight()) return
        checkLayout(popupLayout ?: this.popupLayout)
        if (!handleViewVisibility(view)) return

        highlighter.highlight(view, parent, highlightType, overlayColor, blurRadius)
        post {
            showCurtain()
            showPopup(view.locationOnScreenRect, popupLayout)
        }
    }

    /**
     * Highlight the [item] from [menu].
     * Also, show the popup with some explanation.
     * Note, that highlight will be work only for visible items.
     *
     * @param menuOwner [View], that contains menu
     * @param item id of [MenuItem] to explain
     * @param popupLayout [Int] layout of popup specified for concrete state.
     * If not specified, will be used default layout of popup.
     */
    private fun explainMenuItem(menuOwner: View,
                                @IdRes item: Int,
                                @LayoutRes popupLayout: Int?) {
        menuOwner.takeIf { handleViewVisibility(it) }?.post {
            MenuViewExtractor.extract(menuOwner, item) { extractedView ->
                extractedView?.let { explainView(it, popupLayout) }
                        ?: dispatchError("Can't reach menu item (id: $item) view..")
            }
        }
    }

    /**
     * Highlight the item of [RecyclerView].
     * Also, show the popup with some explanation.
     *
     * @param recyclerView [RecyclerView] instance
     * @param itemPosition position of item to highlight
     * @param popupLayout layout of popup specified for concrete state.
     * If not specified, will be used default layout of popup.
     */
    private fun explainRecyclerItem(recyclerView: RecyclerView,
                                    itemPosition: Int,
                                    @LayoutRes popupLayout: Int?) {
        recyclerView.takeIf { handleViewVisibility(it) }?.run {
            post {
                (layoutManager as? LinearLayoutManager)?.run {
                    scrollToPositionWithOffset(itemPosition,
                            (width / HALF_DIVIDER).takeIf {
                                orientation == RecyclerView.VERTICAL
                            } ?: (height / HALF_DIVIDER))
                } ?: scrollToPosition(itemPosition)
                post {
                    findViewHolderForAdapterPosition(itemPosition)?.itemView?.let { holderView ->
                        explainView(holderView, popupLayout)
                    } ?: dispatchError("Can't reach RecyclerView item view by position $itemPosition..")
                }
            }
        }
    }

    /**
     * Highlight a part of [parent] [View].
     * If [parent] is null, then will be highlighted [Path] with absolute screen coordinates.
     * Also, show the popup with some explanation.
     *
     * @param path [Path] with coordinates to highlight
     * @param parent parent view of [path] or null
     * @param popupLayout layout of popup specified for concrete state.
     * If not specified, will be used default layout of popup.
     */
    private fun explainPath(path: Path,
                            parent: View?,
                            @LayoutRes popupLayout: Int?) {
        if (checkHighlight()) return
        checkLayout(popupLayout ?: this.popupLayout)
        parent?.let {
            handleViewVisibility(it)
            path.run {   // Make coordinates of rect relative to parent view.
                offset(it.leftLocationInWindow.toFloat(), it.topLocationInWindow.toFloat())
            }
        }

        highlighter.highlight(path, this.parent, highlightType, overlayColor, blurRadius)
        post {
            showCurtain()
            showPopup(path.bounds, popupLayout)
        }
    }

    private fun showPopup(rect: Rect, popupLayout: Int?) {
        inflater.inflate(popupLayout ?: this.popupLayout, null)?.also { popupView ->
            dispatchPopupInflated(popupView)
        }?.let { contentView ->
            popup = DynamicPopupWindow(contentView,
                    height,
                    width,
                    popupCornerRadius).apply {
                appearAnimRes = popupAppearAnimationRes
                disappearAnimRes = popupDisappearAnimationRes
                appearAnimatorRes = popupAppearAnimatorRes
                disappearAnimatorRes = popupDisappearAnimatorRes
                showAsDropDown(rect, parent, popupXOffset, popupYOffset)
                isOutsideTouchable = false
            }
        }
    }

    /**
     * Check is [view] displayed on screen.
     *
     * @param view [View] to check
     *
     * @return is [View] visible and is [View] in visible area of screen
     */
    private fun handleViewVisibility(view: View) =
            (view.visibility == View.VISIBLE).also { isVisible ->
                if (isVisible) { // scroll to view if it's 'out of screen'.
                    handleViewScroll(view)
                } else {
                    dispatchError("Cannot highlight hidden view. First make it visible.")
                }
            }

    private fun handleViewScroll(view: View) {
        view.takeUnless { it.isInVisibleArea() }
                ?.scrollToThis(screenWidth / HALF_DIVIDER,
                        screenHeight / HALF_DIVIDER)
    }

    private fun checkLayout(layout: Int) {
        if (layout == NO_LAYOUT) throw IllegalArgumentException("First specify layout ID")
    }

    private fun checkHighlight() = (highlighter.isHighlighted()).applyIf({ it }) {
        dispatchError("View is already highlighted. First dismiss current highlight.")
    }
}