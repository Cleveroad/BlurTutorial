package com.cleveroad.blur_tutorial.sample.models

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class PlanModel(val name: String,
                     val quantity: String,
                     @ColorRes val titleColorRes: Int,
                     @DrawableRes val imageRes: Int)