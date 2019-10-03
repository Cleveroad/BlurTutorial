package com.cleveroad.blur_tutorial.explanator.extract.reflectors

import android.view.View

internal interface MenuReflector {

    /**
     * Extract menu item [View] from [view]'s menu.
     *
     * @param view [View] view, that contains menu
     * @param itemId [Int] id of Menu item
     * @param onViewExtracted triggered, when [View] is extracted
     *
     * @return menu item [View] instance
     */
    fun extractMenuItemView(view: View, itemId: Int, onViewExtracted: (View?) -> Unit)
}