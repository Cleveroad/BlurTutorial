package com.cleveroad.blur_tutorial.ext

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.widget.ImageView

internal fun ImageView.setDrawableColor(color: Int) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        @Suppress("DEPRECATION")
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    } else {
        drawable.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_IN)
    }
}