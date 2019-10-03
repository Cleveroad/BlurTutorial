package com.cleveroad.blur_tutorial.ext

import android.view.View
import android.view.ViewGroup

/**
 * List of children of this [ViewGroup]
 */
internal val ViewGroup.children
    get() = mutableListOf<View>().also { childrenList ->
        for (i in 0 until childCount) childrenList.add(getChildAt(i))
    }.toList()