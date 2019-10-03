package com.cleveroad.blur_tutorial.ext

import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF

internal val Path.bounds: Rect
    get() = RectF().let { boundsRect ->
        computeBounds(boundsRect, false)
        boundsRect.toRect()
    }

private fun RectF.toRect() = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())