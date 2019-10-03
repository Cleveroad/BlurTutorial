package com.cleveroad.blur_tutorial

import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import com.cleveroad.blur_tutorial.ext.getRoot
import com.cleveroad.blur_tutorial.listener.TutorialListener
import com.cleveroad.blur_tutorial.state.TutorialConfig

/**
 * Use it to configure [BlurTutorial] instance.
 */
class TutorialBuilder {

    private var listener: TutorialListener? = null

    internal var config = TutorialConfig()

    internal var blurTutorial: BlurTutorialImpl? = null

    /**
     * Add configuration field to [BlurTutorial].
     *
     * @param parent [View] Root of your layout. Will be blurred or dimmed.
     *
     * @return [TutorialBuilder] instance
     */
    fun withParent(parent: View) = apply { config.parent = parent.getRoot() }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * Note, if you set popup appearing [AnimRes] and popup appearing [AnimatorRes], then
     * will be used only [AnimatorRes].
     *
     * @param animRes [Int] popup appear animation resource.
     *
     * @return [TutorialBuilder] instance
     */
    fun withPopupAppearAnimation(@AnimRes animRes: Int) = apply { config.popupAppearAnimation = animRes }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * Note, if you set popup disappearing [AnimRes] and popup disappearing [AnimatorRes], then
     * will be used only [AnimatorRes].
     *
     * @param animRes [Int] popup disappear animation resource.
     *
     * @return [TutorialBuilder] instance
     */
    fun withPopupDisappearAnimation(@AnimRes animRes: Int) = apply { config.popupDisappearAnimation = animRes }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * Note, if you set popup appearing [AnimRes] and popup appearing [AnimatorRes], then
     * will be used only [AnimatorRes].
     *
     * @param animRes [Int] popup appear animator resource.
     *
     * @return [TutorialBuilder] instance
     */
    fun withPopupAppearAnimator(@AnimatorRes animRes: Int) = apply { config.popupAppearAnimator = animRes }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * Note, if you set popup disappearing [AnimRes] and popup disappearing [AnimatorRes], then
     * will be used only [AnimatorRes].
     *
     * @param animRes [Int] popup disappear animator resource.
     *
     * @return [TutorialBuilder] instance
     */
    fun withPopupDisappearAnimator(@AnimatorRes animRes: Int) = apply { config.popupDisappearAnimator = animRes }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * @param xOffset [Int] x offset of popup window.
     *
     * @return [TutorialBuilder] instance
     */
    fun withPopupXOffset(xOffset: Int) = apply { config.popupXOffset = xOffset }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * @param yOffset [Int] y offset of popup window.
     *
     * @return [TutorialBuilder] instance
     */
    fun withPopupYOffset(yOffset: Int) = apply { config.popupYOffset = yOffset }


    /**
     * Add configuration field to [BlurTutorial].
     *
     * @param radius [Float] corner radius of popup.
     *
     * @return [TutorialBuilder] instance
     */
    fun withPopupCornerRadius(radius: Float) = apply { config.popupCornerRadius = radius }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * @param layout [Int] layout of popup window.
     *
     * @return [TutorialBuilder] instance
     */
    fun withPopupLayout(@LayoutRes layout: Int) = apply { config.popupLayout = layout }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * @param radius [Float] radius of blur. By default, it's 10.0.
     *
     * @return [TutorialBuilder] instance
     */
    fun withBlurRadius(radius: Float) = apply { config.blurRadius = radius }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * @param color [Int] color of overlay, that will be displayed above blurred or dimmed background.
     *
     * @return [TutorialBuilder] instance
     */
    fun withOverlayColor(@ColorInt color: Int) = apply { config.overlayColor = color }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * @param type [HighlightType] type of highlight: Blur or Dim.
     *
     * @return [TutorialBuilder] instance
     */
    fun withHighlightType(type: HighlightType) = apply { config.highlightType = type }

    /**
     * Add configuration field to [BlurTutorial].
     *
     * @param listener [TutorialListener] Listener of tutorial process.
     *
     * @return [TutorialBuilder] instance
     */
    fun withListener(listener: TutorialListener) = apply { this.listener = listener }

    /**
     * Build the [BlurTutorial] instance.
     *
     * @return [BlurTutorial] instance.
     */
    fun build(): BlurTutorial =
            blurTutorial?.also { tutorial ->
                tutorial.config = config
                listener?.let { tutorial.listener = it }
            } ?: BlurTutorialImpl(config, listener)
}