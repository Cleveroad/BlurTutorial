package com.cleveroad.blur_tutorial.sample.utils

import android.view.animation.Animation

open class SimpleAnimationListener : Animation.AnimationListener {

    override fun onAnimationRepeat(animation: Animation?) = Unit

    override fun onAnimationEnd(animation: Animation?) = Unit

    override fun onAnimationStart(animation: Animation?) = Unit
}