package com.cleveroad.blur_tutorial.explanator.popup

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.Outline
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewOutlineProvider
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import com.cleveroad.blur_tutorial.R
import com.cleveroad.blur_tutorial.ext.*
import com.cleveroad.blur_tutorial.utils.HALF_DIVIDER
import com.cleveroad.blur_tutorial.utils.TAG

internal class DynamicPopupWindow(contentView: View,
                                  private val regionHeight: Int,
                                  private val regionWidth: Int,
                                  cornerRadius: Float,
                                  width: Int = WRAP_CONTENT,
                                  height: Int = WRAP_CONTENT,
                                  focusable: Boolean = false) : PopupWindow(contentView, width, height, focusable) {

    companion object {
        const val NO_ANIMATION = -1

        private const val START = 0
        private const val TOP = 0
        private const val SCREEN_TOP = 0
    }

    private val contentOutlineProvider = object: ViewOutlineProvider() {

        override fun getOutline(view: View, outline: Outline?) {
            outline?.setRoundRect(START, TOP, view.width, view.height, cornerRadius)
            view.clipToOutline = true
        }
    }

    private var inflater: LayoutInflater? = null

    private var flContent: FrameLayout? = null
    private var ivTopArrow: ImageView? = null
    private var ivBottomArrow: ImageView? = null

    private var contentBackgroundColor: Int? = null

    private var direction: Direction? = null

    @AnimRes
    var appearAnimRes: Int = NO_ANIMATION

    @AnimRes
    var disappearAnimRes: Int = NO_ANIMATION

    @AnimatorRes
    var appearAnimatorRes: Int = NO_ANIMATION
    set(value) {
        if (value != NO_ANIMATION) {
            field = value
            appearAnimRes = NO_ANIMATION
        }
    }

    @AnimatorRes
    var disappearAnimatorRes: Int = NO_ANIMATION
    set(value) {
        if (value != NO_ANIMATION) {
            field = value
            disappearAnimRes = NO_ANIMATION
        }
    }

    init {
        initInflater(contentView.context)
        setupContent()
    }

    override fun dismiss() {
        applyAnimation(disappearAnimRes, disappearAnimatorRes, onFinishAnim = { super.dismiss() })
    }

    /**
     * Display popup anchored to [anchor] rect.
     * By default, it anchored to bottom of [anchor].
     * If there is no place, it will be anchored to top of [anchor].
     *
     * @param anchor [Rect] rect to anchor
     * @param root [View] Root of layout
     * @param xOff [Int] popup x offset
     * @param yOff [Int] popup y offset
     */
    fun showAsDropDown(anchor: Rect, root: View, xOff: Int, yOff: Int) {
        showAtLocation(root, Gravity.NO_GRAVITY, root.width, root.height)
        contentView.run {
            post {
                checkPopupHeight(anchor)
                direction = calculateDirection(anchor.bottom, anchor.top)
                alignHorizontal(anchor.left, anchor.right)
                post {
                    setArrow(anchor.exactCenterX(), xOff + leftLocationOnScreen)
                    when (direction) {
                        Direction.BOTTOM ->
                            updateLocation(leftLocationOnScreen,
                                    anchor.top - height - yOff)
                        Direction.TOP ->
                            updateLocation(leftLocationOnScreen,
                                    anchor.bottom + yOff)
                        Direction.NO_MATTER ->
                            updateLocation(anchor.centerX(), anchor.centerY())
                    }
                    applyAnimation(appearAnimRes, appearAnimatorRes, onStartAnim = { show() })
                }
            }
        }
    }

    private fun setupContent() {
        inflater?.inflate(R.layout.popup_window_wrapper, null)?.let {
            flContent = it.findViewById(R.id.flContent)
            ivBottomArrow = it.findViewById(R.id.ivArrowBottom)
            ivTopArrow = it.findViewById(R.id.ivArrowTop)

            flContent?.addView(contentView)
            flContent?.outlineProvider = contentOutlineProvider

            contentBackgroundColor = (contentView?.background as? ColorDrawable)?.color

            contentView = it
        }
    }

    private fun setArrow(targetX: Float, xOff: Int) {
        when (direction) {
            Direction.TOP, Direction.NO_MATTER -> {
                ivTopArrow?.run {
                    show()
                    post { x = targetX - xOff - width / HALF_DIVIDER }
                    contentBackgroundColor?.let { setDrawableColor(it) }
                }
                ivBottomArrow?.hide()
            }
            Direction.BOTTOM -> {
                ivBottomArrow?.run {
                    show()
                    post { x = targetX - xOff - width / HALF_DIVIDER }
                    contentBackgroundColor?.let { setDrawableColor(it) }
                }
                ivTopArrow?.hide()
            }
        }
    }

    private fun updateLocation(x: Int,
                               y: Int,
                               width: Int = contentView.width,
                               height: Int = contentView.height) = update(x, y, width, height)

    private fun calculateDirection(bottom: Int, top: Int) = when {
        bottom + contentView.height < regionHeight -> Direction.TOP
        top - contentView.height > SCREEN_TOP -> Direction.BOTTOM
        else -> Direction.NO_MATTER
    }

    private fun alignHorizontal(left: Int, right: Int) =
            // First try anchor to center, then to left, then to right
            tryConstraintToCenter((left + right) / HALF_DIVIDER)
                    || tryConstraintToLeft(left)
                    || tryConstraintToRight(right)

    private fun tryConstraintToCenter(center: Int): Boolean {
        val left = center - contentView.width / HALF_DIVIDER
        val right = center + contentView.width / HALF_DIVIDER
        return (left >= 0 && right <= regionWidth).applyIf({ it }) {
            updateLocation(left, contentView.top)
        }
    }

    private fun tryConstraintToLeft(left: Int): Boolean =
            (left + contentView.width < regionWidth).applyIf({ it }) {
                updateLocation(left, contentView.top)
            }

    private fun tryConstraintToRight(right: Int): Boolean =
            (right - contentView.width > 0).applyIf({ it }) {
                updateLocation(right - contentView.width, contentView.top)
            }


    private fun checkPopupHeight(anchor: Rect) {
        anchor.run {
            if (bottom + contentView.height > regionHeight
                    && top - contentView.height < 0
                    && contentView.height > anchor.height()) {
                Log.w(TAG, "Your popup view height is too large, it may be displayed incorrect")
            }
        }
    }

    private fun initInflater(context: Context?) {
        context?.let { inflater = LayoutInflater.from(it) }
    }

    private fun applyAnimation(@AnimRes animRes: Int,
                               @AnimatorRes animatorRes: Int,
                               onStartAnim: () -> Unit = {},
                               onFinishAnim: () -> Unit = {}) {
        when {
            animRes == NO_ANIMATION && animatorRes == NO_ANIMATION -> {
                onStartAnim()
                onFinishAnim()
            }
            animRes != NO_ANIMATION -> loadAnimation(animRes, onStartAnim, onFinishAnim)
            animatorRes != NO_ANIMATION -> loadAnimator(animatorRes, onStartAnim, onFinishAnim)
        }
    }

    private fun loadAnimation(@AnimRes animRes: Int,
                              onStartAnim: () -> Unit,
                              onFinishAnim: () -> Unit) {
        AnimationUtils.loadAnimation(contentView.context, animRes)?.let { animation ->
            animation.setAnimationListener(object: Animation.AnimationListener {

                override fun onAnimationRepeat(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) = onFinishAnim()

                override fun onAnimationStart(animation: Animation?) = onStartAnim()

            })
            contentView.startAnimation(animation)
        }
    }

    private fun loadAnimator(@AnimatorRes animRes: Int,
                             onStartAnim: () -> Unit,
                             onFinishAnim: () -> Unit) {
        AnimatorInflater.loadAnimator(contentView.context, animRes)?.run {
            addListener(object: Animator.AnimatorListener {

                override fun onAnimationRepeat(animation: Animator?) = Unit

                override fun onAnimationEnd(animation: Animator?) = onFinishAnim()

                override fun onAnimationCancel(animation: Animator?) = onFinishAnim()

                override fun onAnimationStart(animation: Animator?) = onStartAnim()
            })
            setTarget(contentView)
            start()
        }
    }
}