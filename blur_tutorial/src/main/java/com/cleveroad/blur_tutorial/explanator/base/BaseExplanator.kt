package com.cleveroad.blur_tutorial.explanator.base

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.PopupWindow
import com.cleveroad.blur_tutorial.HighlightType
import com.cleveroad.blur_tutorial.R
import com.cleveroad.blur_tutorial.explanator.Explanator
import com.cleveroad.blur_tutorial.explanator.ExplanatorCallback
import com.cleveroad.blur_tutorial.ext.screenHeight
import com.cleveroad.blur_tutorial.ext.screenWidth
import com.cleveroad.blur_tutorial.state.TutorialConfig
import java.lang.ref.WeakReference

internal abstract class BaseExplanator(internal var config: TutorialConfig,
                                       explanatorCallback: ExplanatorCallback) : Explanator {

    companion object {
        private const val NO_SIZE = 0

        private const val START = 0
        private const val TOP = 0
    }

    private val callbackRef = WeakReference(explanatorCallback)

    protected val inflater: LayoutInflater
        get() = LayoutInflater.from(context)

    protected val parent
        get() = config.parent ?: throw IllegalStateException("Parent should not be null!")

    protected val context
        get() = config.parent?.context
                ?: throw IllegalStateException("Parent should not be null!")

    protected val height
        get() = config.parent?.height ?: NO_SIZE

    protected val width
        get() = config.parent?.width ?: NO_SIZE

    protected val screenWidth
        get() = config.parent?.screenWidth ?: NO_SIZE

    protected val screenHeight
        get() = config.parent?.screenHeight ?: NO_SIZE

    protected val resources
        get() = config.parent?.resources

    /**
     * Animation resource of popup appearing
     */
    protected val popupAppearAnimationRes: Int
        get() = config.popupAppearAnimation

    /**
     * Animation resource of popup disappearing
     */
    protected val popupDisappearAnimationRes: Int
        get() = config.popupDisappearAnimation

    /**
     * Animator resource of popup appearing
     */
    protected val popupAppearAnimatorRes: Int
        get() = config.popupAppearAnimator

    /**
     * Animator resource of popup disappearing
     */
    protected val popupDisappearAnimatorRes: Int
        get() = config.popupDisappearAnimator

    /**
     * Y offset of popup (in pixels).
     */
    protected val popupYOffset: Int
        get() = config.popupYOffset

    /**
     * X offset of popup (in pixels).
     */
    protected val popupXOffset: Int
        get() = config.popupXOffset

    /**
     * Corner radius of popup.
     */
    protected val popupCornerRadius: Float
        get() = config.popupCornerRadius

    /**
     * Layout of popup.
     */
    protected val popupLayout: Int
        get() = config.popupLayout

    /**
     * Radius of blur.
     */
    protected val blurRadius: Float
        get() = config.blurRadius

    /**
     * Type of view highlighting.
     */
    protected val highlightType: HighlightType
        get() = config.highlightType

    /**
     * Color of highlight overlay
     */
    protected val overlayColor: Int
        get() = config.overlayColor

    protected var foreground: Drawable? = null
        set(value) {
            value?.let {
                config.parent?.post {
                    it.setBounds(START, TOP, width, height)
                    config.parent?.overlay?.add(it)
                }
            } ?: field?.let { config.parent?.overlay?.remove(it) }
            field = value
        }

    private var curtainPopup: PopupWindow? = null

    protected var menu: Menu? = null

    protected fun showCurtain() {
        if (curtainPopup == null) {
            curtainPopup = PopupWindow(inflater.inflate(R.layout.popup_curtain, null),
                    MATCH_PARENT, MATCH_PARENT)
                    .apply { showAtLocation(parent, Gravity.CENTER, 0, 0) }
        }
    }

    protected fun hideCurtain() {
        curtainPopup?.dismiss()
        curtainPopup = null
    }

    protected fun invalidate() {
        config.parent?.invalidate()
    }

    protected fun post(action: () -> Unit) =
            config.parent?.post(action)

    protected fun dispatchError(errMessage: String) {
        callbackRef.get()?.onError(errMessage)
    }

    protected fun dispatchPopupInflated(popupView: View) {
        callbackRef.get()?.onPopupInflated(popupView)
    }
}