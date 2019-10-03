package com.cleveroad.blur_tutorial.sample.extensions

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import androidx.annotation.ColorRes

fun View.setDrawableColorRes(@ColorRes colorRes: Int) = background.run {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        @Suppress("DEPRECATION")
        setColorFilter(context.getColor(colorRes), PorterDuff.Mode.SRC_IN)
    } else {
        colorFilter = BlendModeColorFilter(context.getColor(colorRes), BlendMode.SRC_IN)
    }
}
