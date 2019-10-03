package com.cleveroad.blur_tutorial.state.tutorial

import android.view.View
import androidx.annotation.IdRes

/**
 * Realisation of [TutorialState] for menu items.
 */
data class MenuState(override val id: Int,
                     val menuOwner: View,
                     @IdRes val menuItemId: Int,
                     override val popupLayout: Int? = null) : TutorialState