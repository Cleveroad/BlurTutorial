package com.cleveroad.blur_tutorial.sample.models

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class ActivityModel(val name: String,
                         @ColorRes val background: Int,
                         @DrawableRes val icon: Int)