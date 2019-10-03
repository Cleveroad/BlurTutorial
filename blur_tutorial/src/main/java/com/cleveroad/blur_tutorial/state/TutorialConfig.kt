package com.cleveroad.blur_tutorial.state

import android.graphics.Color
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import com.cleveroad.blur_tutorial.HighlightType
import com.cleveroad.blur_tutorial.explanator.popup.DynamicPopupWindow.Companion.NO_ANIMATION

internal data class TutorialConfig(var parent: View? = null,
                                   @AnimRes var popupAppearAnimation: Int = NO_ANIMATION,
                                   @AnimRes var popupDisappearAnimation: Int = NO_ANIMATION,
                                   @AnimatorRes var popupAppearAnimator: Int = NO_ANIMATION,
                                   @AnimatorRes var popupDisappearAnimator: Int = NO_ANIMATION,
                                   var popupXOffset: Int = NO_OFFSET,
                                   var popupYOffset: Int = NO_OFFSET,
                                   var popupCornerRadius: Float = NO_RADIUS,
                                   @LayoutRes var popupLayout: Int = NO_LAYOUT,
                                   var blurRadius: Float = DEFAULT_BLUR_RADIUS,
                                   @ColorInt var overlayColor: Int = Color.TRANSPARENT,
                                   var highlightType: HighlightType = HighlightType.BLUR) {

    companion object {
        const val NO_OFFSET = 0
        const val NO_LAYOUT = -1
        const val NO_RADIUS = 0F
        const val DEFAULT_BLUR_RADIUS = 10F
    }
}