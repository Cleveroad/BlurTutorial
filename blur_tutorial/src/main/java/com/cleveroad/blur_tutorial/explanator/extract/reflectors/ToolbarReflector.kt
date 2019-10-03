package com.cleveroad.blur_tutorial.explanator.extract.reflectors

import android.view.View
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.Toolbar
import com.cleveroad.blur_tutorial.ext.children
import com.cleveroad.blur_tutorial.ext.getPrivateField

/**
 * Realization of [MenuReflector] for [Toolbar].
 */
internal class ToolbarReflector: MenuReflector {

    companion object {
        private const val MENU_VIEW_FIELD_NAME = "mMenuView"
    }

    override fun extractMenuItemView(view: View, itemId: Int, onViewExtracted: (View?) -> Unit) {
        (view.getPrivateField(MENU_VIEW_FIELD_NAME) as? ActionMenuView)?.run {
            children.mapNotNull { it as? ActionMenuItemView }.find { it.itemData.itemId == itemId }
        }.let {
            onViewExtracted(it)
        }
    }
}