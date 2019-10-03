package com.cleveroad.blur_tutorial.state.tutorial

import android.view.View

/**
 * Realisation of [TutorialState] for views.
 */
data class ViewState(override val id: Int,
                     val view: View,
                     override val popupLayout: Int? = null) : TutorialState