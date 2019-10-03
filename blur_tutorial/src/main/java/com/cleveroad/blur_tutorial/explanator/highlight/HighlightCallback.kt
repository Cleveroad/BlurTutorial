package com.cleveroad.blur_tutorial.explanator.highlight

import android.graphics.drawable.Drawable

internal interface HighlightCallback {

    /**
     * Called when [Drawable] with highlighted View is generated.
     *
     * @param drawable [Drawable] with highlighted View
     */
    fun onHighlight(drawable: Drawable)
}