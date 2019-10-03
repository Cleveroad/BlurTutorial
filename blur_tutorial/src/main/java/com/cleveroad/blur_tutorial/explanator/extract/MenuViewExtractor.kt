package com.cleveroad.blur_tutorial.explanator.extract

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import com.cleveroad.blur_tutorial.explanator.extract.reflectors.*
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

internal object MenuViewExtractor {

    /**
     * Get [View] instance of menu item.
     *
     * @param menuOwner [View], that contains menu
     * @param itemId [Int] id of menu item
     * @param onExtracted triggered, when [View] is extracted
     *
     * @return [View] of menu item
     */
    fun extract(menuOwner: View, @IdRes itemId: Int, onExtracted: (View?) -> Unit) {
        getReflector(menuOwner)?.extractMenuItemView(menuOwner, itemId, onExtracted)
    }

    private fun getReflector(menuOwner: View): MenuReflector? = when (menuOwner) {
        is BottomAppBar -> BottomAppbarReflector()
        is Toolbar -> ToolbarReflector()
        is BottomNavigationView -> BottomNavigationReflector()
        is NavigationView -> NavigationViewReflector()
        else -> null
    }
}